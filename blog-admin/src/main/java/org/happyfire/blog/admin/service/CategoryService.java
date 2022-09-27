package org.happyfire.blog.admin.service;


import org.happyfire.blog.admin.pojo.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findCategoryByName(String condition);
}
