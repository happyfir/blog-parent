package org.happyfire.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.happyfire.blog.admin.mapper.*;
import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.pojo.*;
import org.happyfire.blog.admin.service.ArticleService;
import org.happyfire.blog.admin.service.CategoryService;
import org.happyfire.blog.admin.service.Sys_userService;
import org.happyfire.blog.admin.service.TagService;
import org.happyfire.blog.admin.vo.ArticleVo;
import org.happyfire.blog.admin.vo.PageResult;
import org.happyfire.blog.admin.vo.Result;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private Sys_userService sysUserService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticlesMapper articlesMapper;
    @Autowired
    private Sys_userMapper sys_userMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private CommentMapper commentMapper;

    /**
     * 获取文章信息
     * @return
     */

    @Override
    public Result getArticles(PageParam pageParam) {
        String condition = pageParam.getQueryString();
        Page<Article> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
        LambdaQueryWrapper<Article> searchQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNoneBlank(condition)){
            searchQueryWrapper.like(Article::getTitle,condition)
                    .or().like(Article::getSummary,condition);

            //如果查询的条件是作者 标签 分类 需要进行转化
            //查询条件是作者
            List<Sys_user> userList = sysUserService.findUserBynickName(condition);
            for (Sys_user sysUser : userList) {
                searchQueryWrapper.or().eq(Article::getAuthorId,sysUser.getId());
            }

            //查询条件是分类
            List<Category> categoryList = categoryService.findCategoryByName(condition);
            for (Category category : categoryList) {
                searchQueryWrapper.or().eq(Article::getCategoryId,category.getId());
            }

            //查询条件是标签 多对多关系
            List<ArticleTag> articleTagList = new ArrayList<>();
            List<Tag> tagList = tagService.findTagsByArticleName(condition);
            for (Tag tag : tagList) {
                LambdaQueryWrapper<ArticleTag> articleTagsLambdaQueryWrapper = new LambdaQueryWrapper<>();
                articleTagsLambdaQueryWrapper.eq(ArticleTag::getTagId,tag.getId());
                articleTagList = articleTagMapper.selectList(articleTagsLambdaQueryWrapper);
            }
            for (ArticleTag articleTag : articleTagList) {
                searchQueryWrapper.or().like(Article::getId,articleTag.getArticleId());
            }
        }
        Page<Article> articlePage = articlesMapper.selectPage(page, searchQueryWrapper);

        List<Article> articleRecords = articlePage.getRecords();
        List<ArticleVo> articleVoList = copyList(articleRecords);

        PageResult<ArticleVo> pageResult = new PageResult<>();
        pageResult.setList(articleVoList);
        pageResult.setTotal(articlePage.getTotal());
        return Result.success(pageResult);
    }

    /**
     * 删除文章
     * @param id
     * @return
     */
    @Override
    public Result delete(Long id) {
        //首先获取文章主体
        Article article = articlesMapper.selectById(id);
        //获取关联数据
        //获取文章本体 并删除
        Long articleBodyId = article.getBodyId();
        articleBodyMapper.deleteById(articleBodyId);
        //删除文章与标签之间的联系
        LambdaQueryWrapper<ArticleTag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ArticleTag::getArticleId,id);
        articleTagMapper.delete(lambdaQueryWrapper);
        //删除文章对应的评论
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getArticleId,id);
        commentMapper.delete(commentLambdaQueryWrapper);
        //最后、删除该文章记录
        articlesMapper.deleteById(id);
        return Result.success(null);
    }

    /**
     * 查找某一分类下存在的文章数目
     * @param id
     * @return
     */
    @Override
    public int findArticlesByCategoryId(Long id) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Article::getCategoryId,id);
        List<Article> articles = articlesMapper.selectList(lambdaQueryWrapper);
        return articles.size();
    }

    private List<ArticleVo> copyList(List<Article> records){
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article){
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        articleVo.setAuthorId(String.valueOf(article.getAuthorId()));
        BeanUtils.copyProperties(article,articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //设置作者账户与昵称
        //查找作者
        Sys_user sys_user = sys_userMapper.selectById(article.getAuthorId());
        articleVo.setNickName(sys_user.getNickname());
        articleVo.setAccount(sys_user.getAccount());
        //查找分类
        Category category = categoryMapper.selectById(article.getCategoryId());
        articleVo.setCategory(category);
        //查找标签
        StringBuilder tagStr = new StringBuilder();
        LambdaQueryWrapper<ArticleTag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
        List<ArticleTag> articleTags = articleTagMapper.selectList(lambdaQueryWrapper);
        for (ArticleTag articleTag : articleTags) {
            Long tagId = articleTag.getTagId();
            Tag tag = tagMapper.selectById(tagId);
            tagStr.append(tag.getTagName());
            tagStr.append(",");
        }
        tagStr.deleteCharAt(tagStr.length()-1);
        articleVo.setTag(tagStr.toString());
        return articleVo;
    }

}
