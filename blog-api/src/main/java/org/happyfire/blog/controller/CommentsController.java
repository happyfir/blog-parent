package org.happyfire.blog.controller;

import org.happyfire.blog.common.aop.LogAnnotation;
import org.happyfire.blog.service.CommentsService;
import org.happyfire.blog.vo.Result;
import org.happyfire.blog.vo.param.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentsController {
    @Autowired
    private CommentsService commentsService;

    @LogAnnotation(module = "评论", operation = "获取评论列表")
    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long id){
        return commentsService.commentsByArticleId(id);

    }

    @LogAnnotation(module = "发送评论", operation = "获取发送的评论")
    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentsService.comment(commentParam);
    }
}
