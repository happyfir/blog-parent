package org.happyfire.blog.controller;

import org.happyfire.blog.common.aop.LogAnnotation;
import org.happyfire.blog.common.cache.Cache;
import org.happyfire.blog.service.ArticleService;
import org.happyfire.blog.vo.Result;
import org.happyfire.blog.vo.param.ArticleParam;
import org.happyfire.blog.vo.param.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//json数据类型交互
@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    /**
     * 首页文章列表
     * @param pageParams
     * @return
     */
    //TODO 新增 标签文章分类 和 文章归档 尚未测试
    @PostMapping
    @LogAnnotation(module = "文章", operation = "获取文章列表")
    @Cache(expire = 5 * 60 * 1000,name = "list_article")
    public Result listArticle(@RequestBody PageParams pageParams){
//        System.out.println("获取的参数" + pageParams);
        return articleService.listArticle(pageParams);
    }


    /**
     * 热门文章
     * @return
     */
    @PostMapping("hot")
    @Cache(expire = 5 * 60 * 1000,name = "hot_article")
    public Result hotArticle(){
        int limit = 5;
        return articleService.hotArticle(limit);
    }

    /**
     * 最新文章
     * @return
     */
    @PostMapping("new")
    @Cache(expire = 5 * 60 * 1000,name = "new_article")
    public Result newArticle(){
        int limit = 5;
        return articleService.newArticle(limit);
    }

    /**
     * 文章归档
     * @return
     */
    @PostMapping("listArchives")
    @Cache(expire = 5 * 60 * 1000,name = "list_archtives")
    public Result listArchives(){
        return articleService.listArchives();
    }

    /**
     * 查看文章
     * @param articleId
     * @return
     */
    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") long articleId){
        return articleService.findArticleById(articleId);
    }

    /**
     * 发布文章
     * @param articleParam
     * @return
     */
    //TODO 还没有测试 需要后续进行测试
    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }

}
