package org.happyfire.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.happyfire.blog.admin.mapper.Sys_userMapper;
import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.pojo.Sys_user;
import org.happyfire.blog.admin.service.SecurityUserService;
import org.happyfire.blog.admin.service.Sys_userService;
import org.happyfire.blog.admin.vo.PageResult;
import org.happyfire.blog.admin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class Sys_userServiceImpl implements Sys_userService {

    @Autowired
    private Sys_userMapper sys_userMapper;

    @Autowired
    private SecurityUserService securityUserService;

    @Override
    public Result update(Sys_user sysUser) {
        sys_userMapper.updateById(sysUser);
        return Result.success(null);
    }

    @Override
    public Result listUser(PageParam pageParam) {
        Page<Sys_user> page = new Page<>(pageParam.getCurrentPage(),pageParam.getPageSize());
        LambdaQueryWrapper<Sys_user> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNoneBlank(pageParam.getQueryString())){
            lambdaQueryWrapper.eq(Sys_user::getNickname,pageParam.getQueryString())
                    .or().eq(Sys_user::getAccount,pageParam.getQueryString())
                    .or().like(Sys_user::getEmail,pageParam.getQueryString())
                    .or().like(Sys_user::getMobilePhoneNumber,pageParam.getQueryString());
        }
        Page<Sys_user> sys_userPage = sys_userMapper.selectPage(page,lambdaQueryWrapper);
        PageResult<Sys_user> pageResult = new PageResult<>();
        pageResult.setList(sys_userPage.getRecords());
        pageResult.setTotal(sys_userPage.getTotal());
        return Result.success(pageResult);
    }

    @Override
    public Result delete(Long id) {
        this.sys_userMapper.deleteById(id);
        return Result.success(null);
    }

    @Override
    public Result getUserInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User)principal;
        return Result.success(user);
    }
}
