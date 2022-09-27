package org.happyfire.blog.admin.service;

import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.vo.Result;

public interface ArticleService {

    Result getArticles(PageParam pageParam);

    Result delete(Long id);
}
