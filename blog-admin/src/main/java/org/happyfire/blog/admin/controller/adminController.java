package org.happyfire.blog.admin.controller;

import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.pojo.Permission;
import org.happyfire.blog.admin.pojo.Sys_user;
import org.happyfire.blog.admin.service.PermissionService;
import org.happyfire.blog.admin.service.SecurityUserService;
import org.happyfire.blog.admin.service.Sys_userService;
import org.happyfire.blog.admin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户：admin
 * 密码：123456
 */
@RestController
@RequestMapping("admin")
public class adminController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private Sys_userService sysUserService;


    @PostMapping("permission/permissionList")
    public Result listPermission(@RequestBody PageParam pageParam) {
        return permissionService.listPermission(pageParam);
    }

    @PostMapping("permission/add")
    public Result add(@RequestBody Permission permission) {
        return permissionService.add(permission);
    }

    @PostMapping("permission/update")
    public Result update(@RequestBody Permission permission) {
        return permissionService.update(permission);
    }

    @GetMapping("permission/delete/{id}")
    public Result delete(@PathVariable("id") Long id) {
        return permissionService.delete(id);
    }

    @PostMapping("user/userList")
    public Result listUser(@RequestBody PageParam pageParam) {
        return sysUserService.listUser(pageParam);
    }

    @PostMapping("user/update")
    public Result updateUser(@RequestBody Sys_user sysUser){
        return sysUserService.update(sysUser);
    }

    @GetMapping("user/delete/{id}")
    public Result userDelete(@PathVariable("id") Long id) {
        return sysUserService.delete(id);
    }

    @PostMapping("user/userInfo")
    public Result getUserInfo(){
        return sysUserService.getUserInfo();
    }
}
