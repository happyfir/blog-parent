package org.happyfire.blog.service;

import org.happyfire.blog.vo.CategoryVo;
import org.happyfire.blog.vo.Result;

import java.util.List;

public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);

    /**
     * 查找所有类别标签
     * @return
     */
    Result findAll();

    Result findAllDetail();

    Result categoriesDetailById(Long id);
}
