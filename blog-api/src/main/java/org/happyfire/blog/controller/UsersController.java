package org.happyfire.blog.controller;

import org.happyfire.blog.service.SysUserService;
import org.happyfire.blog.vo.Result;
import org.happyfire.blog.vo.param.ChangeParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UsersController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token){
        return sysUserService.findUserByToken(token);
    }

    /**
     * 修改用户信息 未修改密码
     * @param changeParam
     * @return
     */
    @PostMapping("changeUserInfo")
    public Result changeUserInfo(@RequestHeader("Authorization") String token, @RequestBody ChangeParam changeParam){
        return sysUserService.changeUserInfo(token,changeParam);
    }

    /**
     * 修改用户密码 包含其他信息
     * @param changeParam
     * @return
     */
    @PostMapping("changePassword")
    public Result changPassword(@RequestBody ChangeParam changeParam){
        return sysUserService.changePassword(changeParam);
    }
}
