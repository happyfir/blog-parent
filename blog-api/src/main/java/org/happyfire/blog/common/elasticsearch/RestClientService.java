package org.happyfire.blog.common.elasticsearch;

import org.happyfire.blog.dao.pojo.Article;
import org.happyfire.blog.vo.param.PageParams;

import java.util.List;

public interface RestClientService {
    public List<Article> search(PageParams pageParams);
}
