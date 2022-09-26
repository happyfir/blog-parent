package org.happyfire.blog.service;

import org.happyfire.blog.dao.pojo.Tag;
import org.happyfire.blog.vo.Result;
import org.happyfire.blog.vo.TagVo;

import java.util.List;

public interface TagService {
    List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);

    /**
     * 查询所有标签
     * @return
     */
    Result findAll();

    Result findAllDetail();

    Result findDetailById(Long id);

    List<Tag> findTagsByArticleName(String name);
}
