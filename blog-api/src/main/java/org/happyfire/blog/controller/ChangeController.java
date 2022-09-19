package org.happyfire.blog.controller;


import lombok.extern.slf4j.Slf4j;
import org.happyfire.blog.service.ChangeService;
import org.happyfire.blog.vo.Result;
import org.happyfire.blog.vo.param.ChangePasswordParam;
import org.happyfire.blog.vo.param.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("change")
public class ChangeController {
    @Autowired
    private ChangeService changeService;

    /**
     * 修改用户密码
     * @param changePasswordParam
     * @return
     */
    @PostMapping
    public Result changPassword(@RequestBody ChangePasswordParam changePasswordParam){
        System.out.println(changePasswordParam.toString());
        return changeService.changePassword(changePasswordParam);
    }
}
