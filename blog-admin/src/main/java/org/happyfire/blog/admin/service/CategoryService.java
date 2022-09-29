package org.happyfire.blog.admin.service;


import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.pojo.Category;
import org.happyfire.blog.admin.vo.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    List<Category> findCategoryByName(String condition);

    Result findCategories(PageParam pageParam);

    Result update(Category category);

    Result add(Category category);

    Result deleteById(Long id);

    Result uploadAvatar(MultipartFile file, String categoryId);
}
