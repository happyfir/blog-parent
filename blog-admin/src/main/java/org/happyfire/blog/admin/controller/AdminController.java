package org.happyfire.blog.admin.controller;

import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.model.params.PasswordParam;
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

    //TODO  后续完善后台管理系统的权限管理功能
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

    @PostMapping("adminInfo")
    public Result getAdminInfo(){
        return adminService.getAdminInfo();
    }

    /**
     * 重置密码
     * @param admin
     * @return
     */
    @PostMapping("password")
    public Result setPassword(@RequestBody Admin admin) {
//        return adminService.clearPassword(id);
        return adminService.setPassword(admin);
    }

//    @PostMapping("changePassword")
//    public Result changePassword(@RequestBody PasswordParam passwordParam){
//        return adminService.changePassword(passwordParam);
//    }
}
