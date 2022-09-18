package org.happyfire.blog.admin.service.impl;

import org.happyfire.blog.admin.pojo.Admin;
import org.happyfire.blog.admin.service.AdminService;
import org.happyfire.blog.admin.service.SecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class SecurityUserServiceImpl implements SecurityUserService, UserDetailsService {

    @Autowired
    private AdminService adminService;

    /**
     * 登陆时会把username传递到这里，通过username 查询admin表 如果admin存在 将密码告诉spring security
     * 如果不存在返回null 认证失败
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //当用户登录的时候，springSecurity 就会将请求 转发到此
        //根据用户名 查找用户，不存在 抛出异常，存在 将用户名，密码，授权列表 组装成springSecurity的User对象 并返回
        Admin admin = adminService.findAdminByUsername(username);
        if (admin == null){
            //登陆失败
            return null;
        }
        UserDetails userDetails = new User(username, admin.getPassword(), new ArrayList<>());
        return userDetails;
    }
}
