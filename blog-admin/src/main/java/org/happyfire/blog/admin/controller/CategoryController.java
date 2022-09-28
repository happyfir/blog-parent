package org.happyfire.blog.admin.controller;

import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.service.CategoryService;
import org.happyfire.blog.admin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("category")
@Transactional
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    //TODO 完善图片上传功能
    @PostMapping("categoryList")
    public Result listCategories(@RequestBody PageParam pageParam){
        return categoryService.findCategories(pageParam);
    }
}
