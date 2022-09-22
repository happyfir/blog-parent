package org.happyfire.blog.controller;

import org.apache.commons.lang3.StringUtils;
import org.happyfire.blog.service.UploadService;
import org.happyfire.blog.utils.QiniuUtils;
import org.happyfire.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("upload")
public class UploadController {
    @Autowired
    private UploadService uploadService;
    @PostMapping
    public Result upload(@RequestParam("image")MultipartFile file){
        return uploadService.upload(file);
    }

    /**
     * 上传头像
     * @param file
     * @return
     */
    @PostMapping("avatar")
    public Result uploadAvatar(@RequestParam("file")MultipartFile file,@RequestParam Object token){
        return uploadService.uploadAvatar(file,token.toString());
    }
}
