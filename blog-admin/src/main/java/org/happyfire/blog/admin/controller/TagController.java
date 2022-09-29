package org.happyfire.blog.admin.controller;

import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.pojo.Tag;
import org.happyfire.blog.admin.service.TagService;
import org.happyfire.blog.admin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("tag")
@Transactional
public class TagController {
    @Autowired
    private TagService tagService;

    @PostMapping("tagList")
    public Result listTags(@RequestBody PageParam pageParam){
        return tagService.findTags(pageParam);
    }

    @PostMapping("update")
    public Result updateTag(@RequestBody Tag tag){
        return tagService.update(tag);
    }

    @PostMapping("add")
    public Result addTag(@RequestBody Tag tag){
        return tagService.add(tag);
    }

    @GetMapping("delete/{id}")
    public Result deleteTag(@PathVariable("id") Long id){
        return tagService.deleteById(id);
    }

    @PostMapping("uploadAvatar")
    public Result uploadAvatar(@RequestParam("file") MultipartFile file, @RequestParam Object tagId){
        return tagService.uploadAvatar(file,tagId.toString());
    }
}
