package org.happyfire.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.happyfire.blog.dao.mapper.CategoryMapper;
import org.happyfire.blog.dao.pojo.Category;
import org.happyfire.blog.service.CategoryService;
import org.happyfire.blog.vo.CategoryVo;
import org.happyfire.blog.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public CategoryVo findCategoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        categoryVo.setId(String.valueOf(category.getId()));
        return categoryVo;
    }

    @Override
    public Result findAll() {
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<>());
        //页面交互对象
        return Result.success(copyList(categoryList));
    }

    @Override
    public Result findAllDetail() {
        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<>());
        //页面交互的对象
        return Result.success(copyList(categories));
    }

    @Override
    public Result categoriesDetailById(Long id) {
        Category category = categoryMapper.selectById(id);
        CategoryVo categoryVo = copy(category);
        return Result.success(categoryVo);
    }

    /**
     * 根据分类名称查询分类
     * 前端搜索功能会用到
     * @param name
     * @return
     */
    @Override
    public List<Category> findCategoryByName(String name) {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.like(Category::getCategoryName,name);
        List<Category> categories = categoryMapper.selectList(categoryLambdaQueryWrapper);
        return categories;
    }

    public CategoryVo copy(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        categoryVo.setId(String.valueOf(category.getId()));
        return categoryVo;
    }
    public List<CategoryVo> copyList(List<Category> categoryList){
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categoryList) {
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }
}
