package org.happyfire.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.happyfire.blog.admin.mapper.*;
import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.pojo.*;
import org.happyfire.blog.admin.service.ArticleService;
import org.happyfire.blog.admin.vo.ArticleVo;
import org.happyfire.blog.admin.vo.PageResult;
import org.happyfire.blog.admin.vo.Result;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

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
        Page<Article> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNoneBlank(pageParam.getQueryString())){
            //TODO 查询逻辑
            lambdaQueryWrapper.like(Article::getTitle,pageParam.getQueryString())
                    .or().like(Article::getSummary,pageParam.getQueryString());
        }
        Page<Article> articlePage = articlesMapper.selectPage(page, lambdaQueryWrapper);

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
