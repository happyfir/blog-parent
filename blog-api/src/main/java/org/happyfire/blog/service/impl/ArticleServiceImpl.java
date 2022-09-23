package org.happyfire.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.log.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.happyfire.blog.dao.dos.Archives;
import org.happyfire.blog.dao.mapper.*;
import org.happyfire.blog.dao.pojo.Article;
import org.happyfire.blog.dao.pojo.ArticleBody;
import org.happyfire.blog.dao.pojo.ArticleTag;
import org.happyfire.blog.dao.pojo.SysUser;
import org.happyfire.blog.service.*;
import org.happyfire.blog.utils.UserThreadLocal;
import org.happyfire.blog.vo.ArticleBodyVo;
import org.happyfire.blog.vo.ArticleVo;
import org.happyfire.blog.vo.Result;
import org.happyfire.blog.vo.TagVo;
import org.happyfire.blog.vo.param.ArticleParam;
import org.happyfire.blog.vo.param.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 分页查询article数据表
     * @param pageParams
     * @return
     */
    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article> articleIPage = this.articleMapper.listArticle(page,pageParams.getCategoryId(),pageParams.getTagId(),pageParams.getYear(),pageParams.getMonth());
        return Result.success(copyList(articleIPage.getRecords(),true,true));
    }
//    @Override
//    public Result listArticle(PageParams pageParams) {
//        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//
//        //查询文章的参数 加上分类id，判断不为空 加上分类条件
//        if (pageParams.getCategoryId() != null) {
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//
//        //加入标签条件 article中并没有tag字段 先根据tagid查出所有文章id  再加入条件
//        List<Long> articleIdList = new ArrayList<>();
//        if (pageParams.getTagId() != null){
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            for (ArticleTag articleTag : articleTags) {
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if (articleIdList.size() > 0){
//                queryWrapper.in(Article::getId,articleIdList);
//            }
//        }
//
//        //置顶排序
//        queryWrapper.orderByDesc(Article::getWeight);
//        queryWrapper.orderByDesc(Article::getCreateDate);
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
//        List<Article> records = articlePage.getRecords();
//        List<ArticleVo> articleVoList = copyList(records,true,true);
//        return Result.success(articleVoList);
//    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit " + limit );
        List<Article> articleList = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articleList,false,false));
    }

    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit " + limit );
        List<Article> articleList = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articleList,false,false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Override
    public Result findArticleById(long articleId) {
        /**
         * 1、根据Id查询文章信息
         * 2、根据bodyid 和categoryid 关联查询
         */
        Article article = articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true,true,true);
        //线程池操作 把新增阅读数操作放到线程池去操作
        threadService.updateArticleViewCount(articleMapper, article);
        return Result.success(articleVo);
    }

    /**
     * 发布文章
     * @param articleParam
     * @return
     */
    @Override
    public Result publish(ArticleParam articleParam) {
        /**
         *发布文章 目的是构建article对象
         *作者id  此接口要加入到登录拦截中
         *将标签加入到关联列表中
         *body 内容存储
         */


        //文章id
        Long articleId = articleParam.getId();
        //用户id
        SysUser sysUser = UserThreadLocal.get();
        //查看文章id是否存在
        Article currentarticle = articleMapper.selectById(articleId);
        if (currentarticle != null){
            //当前文章存在 更新
            currentarticle.setAuthorId(sysUser.getId());
            currentarticle.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
            currentarticle.setCreateDate(System.currentTimeMillis());
            //TODO 需要对评论等属性进行改造
            currentarticle.setCommentCounts(0);
            currentarticle.setSummary(articleParam.getSummary());
            currentarticle.setTitle(articleParam.getTitle());
            //TODO 需要对阅读数量等属性进行改造
            currentarticle.setViewCounts(0);
            currentarticle.setWeight(Article.Article_Common);
            //获取bodyId
            Long bodyId = currentarticle.getBodyId();
            currentarticle.setBodyId(bodyId);

            //body
            ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
            articleBody.setContent(articleParam.getBody().getContent());
            articleBody.setContentHtml(articleParam.getBody().getContentHtml());
//            articleBody.setArticleId(article.getId());
            articleBodyMapper.updateById(articleBody);

            //更新文章
            articleMapper.updateById(currentarticle);

            ArticleVo articleVo = new ArticleVo();
            articleVo.setId(currentarticle.getId().toString());

            //删除redis中对用的缓存  采用模糊查询
            clearCache();

            return Result.success(articleVo);
        }

        //当前文章不存在 新建
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
        article.setCreateDate(System.currentTimeMillis());
        article.setCommentCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setBodyId(-1L);
        //插入之后会生成一个文章id
        this.articleMapper.insert(article);

        //tags
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(Long.parseLong(tag.getId()));
                this.articleTagMapper.insert(articleTag);
            }
        }

        //body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId().toString());

        //删除redis中对用的缓存  采用模糊查询
        clearCache();

        return Result.success(articleVo);
    }


    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    /**
     * 根据文章id删除文章
     * 软删除 并不是实际删除 而是使其无法被搜索 通过设计一个标记来实现
     *
     * 结果： 由于之前article的方法都是用redis缓存进行统一优化 所以当逻辑删除了文章后  由于缓存的存在，前端并不能实时反馈 需要改进
     * 删除文章时 将redis对的键值对删除
     * @param articleId
     * @return
     */
    @Override
    public Result deleteArticleById(long articleId) {
        //获取文章
        Article article = articleMapper.selectById(articleId);
        //设置删除标志位为1
        article.setDeleted(1);
        articleMapper.updateById(article);
        //删除redis中对用的缓存  采用模糊查询
        clearCache();
        return Result.success(null);
    }

    /**
     * 删除redis中对用的缓存  采用模糊查询
     */
    public void clearCache(){
        String[] prexs = new String[]{"hot_article::ArticleController::hotArticle::",
                "new_article::ArticleController::newArticle::",
                "list_archtives::ArticleController::listArchives::",
                "list_article::ArticleController::listArticle::"};
        for (String prex : prexs) {
            Set<String> keys=redisTemplate.keys(prex + "*");
            if (!keys.isEmpty()){
                redisTemplate.delete(keys);
            }
        }
    }

    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record:records){
            articleVoList.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record:records){
            articleVoList.add(copy(record,isTag,isAuthor, isBody, isCategory));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor, boolean isBody, boolean isCategory){
        ArticleVo articleVo= new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article,articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有接口都需要标签和接口信息
        if (isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthorId(authorId);
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if (isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }

}
