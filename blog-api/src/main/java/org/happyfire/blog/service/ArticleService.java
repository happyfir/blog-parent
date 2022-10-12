package org.happyfire.blog.service;

import org.happyfire.blog.dao.pojo.Article;
import org.happyfire.blog.vo.Result;
import org.happyfire.blog.vo.param.ArticleParam;
import org.happyfire.blog.vo.param.PageParams;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;


public interface ArticleService {
    /**
     * 分页查询文章列表
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);

    /**
     *最热文章
     * @param limit
     * @return
     */
    Result hotArticle(int limit);

    /**
     * 最新文章
     * @param limit
     * @return
     */
    Result newArticle(int limit);

    /**
     * 文章归档
     * @return
     */
    Result listArchives();

    /**
     * 查询文章内容
     * @param articleId
     * @return
     */
    Result findArticleById(long articleId);

    /**
     * 发布文章
     * @param articleParam
     * @return
     */
    Result publish(ArticleParam articleParam);

    Result deleteArticleById(long articleId);

    /**
     * 查找文章
     * @param pageParams
     * @return
     */
    Result searchArticles(PageParams pageParams);

    /**
     * 使用ES查询文章
     * @param pageParams
     * @return
     */
    Result searchArticlesByES(PageParams pageParams);

    /**
     * 搜索所有文章  用于给ES索引库批量导入数据
     */
    List<Article> listArticles();
}
