package org.happyfire.blog.controller;

import org.happyfire.blog.dao.pojo.SysUser;
import org.happyfire.blog.utils.UserThreadLocal;
import org.happyfire.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {
    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}
