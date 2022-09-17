package org.happyfire.blog.controller;

import lombok.extern.slf4j.Slf4j;
import org.happyfire.blog.service.LoginService;
import org.happyfire.blog.vo.Result;
import org.happyfire.blog.vo.param.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("register")
public class RegisterController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result register(@RequestBody LoginParam registerParam){
        log.info(registerParam.getAccount());
        //sso 单点登录 后期如果把登录注册功能提出去作为单独服务（接口服务）
        return loginService.register(registerParam);
    }
}
