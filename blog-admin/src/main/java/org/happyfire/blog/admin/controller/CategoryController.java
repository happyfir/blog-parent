package org.happyfire.blog.admin.controller;

import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.pojo.Category;
import org.happyfire.blog.admin.service.CategoryService;
import org.happyfire.blog.admin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("category")
@Transactional
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("categoryList")
    public Result listCategories(@RequestBody PageParam pageParam){
        return categoryService.findCategories(pageParam);
    }

    @PostMapping("update")
    public Result updateCategory(@RequestBody Category category){
        return categoryService.update(category);
    }

    @PostMapping("add")
    public Result addCategory(@RequestBody Category category){
        return categoryService.add(category);
    }

    @GetMapping("delete/{id}")
    public Result deleteCategory(@PathVariable("id") Long id){
        return categoryService.deleteById(id);
    }

    @PostMapping("uploadAvatar")
    public Result uploadAvatar(@RequestParam("file") MultipartFile file, @RequestParam Object categoryId){
        return categoryService.uploadAvatar(file,categoryId.toString());
    }
}
