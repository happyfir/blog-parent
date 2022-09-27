package org.happyfire.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.happyfire.blog.admin.mapper.CategoryMapper;
import org.happyfire.blog.admin.pojo.Category;
import org.happyfire.blog.admin.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public List<Category> findCategoryByName(String name) {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.like(Category::getCategoryName,name);
        List<Category> categories = categoryMapper.selectList(categoryLambdaQueryWrapper);
        return categories;
    }
}
