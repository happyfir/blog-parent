package org.happyfire.blog.service.impl;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.happyfire.blog.dao.pojo.SysUser;
import org.happyfire.blog.service.LoginService;
import org.happyfire.blog.service.SysUserService;
import org.happyfire.blog.service.UploadService;
import org.happyfire.blog.utils.QiniuUtils;
import org.happyfire.blog.vo.ErrorCode;
import org.happyfire.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UploadServiceImpl implements UploadService {
    @Autowired
    private QiniuUtils qiniuUtils;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Result upload(MultipartFile file) {
        //原始文件名称
        String originalFilename = file.getOriginalFilename();
        //唯一的文件名称
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        //上传文件 到七牛云
        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload){
            return Result.success(QiniuUtils.url + fileName);
        }
        return Result.fail(20001,"上传失败");
    }

    @Override
    public Result uploadAvatar(MultipartFile file, String token) {
        //原始文件名称
        String originalFilename = file.getOriginalFilename();
        //唯一的文件名称
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        //上传文件 到七牛云
        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload){
            //将图片地址传给用户
            String url = QiniuUtils.url + fileName;
            SysUser sysUser = loginService.checkToken(token);
            if (sysUser == null){
                return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
            }
            sysUser.setAvatar(url);
            sysUserService.update(sysUser);
            //将修改后的数据存入redis
            SysUser updateUser = sysUserService.findUserById(sysUser.getId());
            System.out.println(updateUser.toString());
            redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(updateUser),1, TimeUnit.DAYS);

            return Result.success(url);
        }
        return Result.fail(20001,"上传失败");
    }
}
