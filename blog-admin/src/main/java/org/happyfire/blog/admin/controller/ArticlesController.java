package org.happyfire.blog.admin.controller;

import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.service.ArticleService;
import org.happyfire.blog.admin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("articles")
@Transactional
public class ArticlesController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("articlesList")
    public Result getArticles(@RequestBody PageParam pageParam) {
        return articleService.getArticles(pageParam);
    }

    @GetMapping("delete/{id}")
    public Result deleteArticle(@PathVariable("id") Long id) {
        return articleService.delete(id);
    }

}
