package org.happyfire.blog.admin.controller;

import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.pojo.Admin;
import org.happyfire.blog.admin.pojo.Permission;
import org.happyfire.blog.admin.pojo.Sys_user;
import org.happyfire.blog.admin.service.AdminService;
import org.happyfire.blog.admin.service.ArticleService;
import org.happyfire.blog.admin.service.PermissionService;
import org.happyfire.blog.admin.service.Sys_userService;
import org.happyfire.blog.admin.vo.AdminVo;
import org.happyfire.blog.admin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 用户：admin
 * 密码：123456
 */

@RestController
@RequestMapping("admin")
@Transactional
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("update")
    public Result updateAdmin(@RequestBody AdminVo adminVo){
        return adminService.update(adminVo);
    }
    @PostMapping("add")
    public Result addAdmin(@RequestBody AdminVo adminVo){
        return adminService.addAdmin(adminVo);
    }

    @PostMapping("adminList")
    public Result adminList(@RequestBody PageParam pageParam){
        return adminService.getAdmins(pageParam);
    }

    @GetMapping("delete/{id}")
    public Result deleteAdmin(@PathVariable("id") Long id) {
        return adminService.deleteById(id);
    }

    /**
     * 重置密码  重置后密码为username账户
     * @param id
     * @return
     */
    @GetMapping("password/{id}")
    public Result clearPassword(@PathVariable("id") Long id) {
        return adminService.clearPassword(id);
    }
}
