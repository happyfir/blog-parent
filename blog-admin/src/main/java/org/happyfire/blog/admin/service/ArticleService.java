package org.happyfire.blog.admin.service;

import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.vo.Result;

public interface ArticleService {

    Result getArticles(PageParam pageParam);

    Result delete(Long id);

    /**
     * 查找某一分类下存在的文章数目
     * @param id
     * @return
     */
    int findArticlesByCategoryId(Long id);
}
