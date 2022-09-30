package org.happyfire.blog.admin.controller;

import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.pojo.Permission;
import org.happyfire.blog.admin.pojo.Sys_user;
import org.happyfire.blog.admin.service.PermissionService;
import org.happyfire.blog.admin.service.Sys_userService;
import org.happyfire.blog.admin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@Transactional
public class UserController {
    @Autowired
    private Sys_userService sysUserService;

    @PostMapping("userList")
    public Result listUser(@RequestBody PageParam pageParam) {
        return sysUserService.listUser(pageParam);
    }

    @PostMapping("update")
    public Result updateUser(@RequestBody Sys_user sysUser){
        return sysUserService.update(sysUser);
    }

    @GetMapping("delete/{id}")
    public Result userDelete(@PathVariable("id") Long id) {
        return sysUserService.delete(id);
    }

//    @PostMapping("userInfo")
//    public Result getUserInfo(){
//        return sysUserService.getUserInfo();
//    }

    @GetMapping("password/{id}")
    public Result clearPassword(@PathVariable("id") Long id){
        return sysUserService.clearPassword(id);
    }
}
