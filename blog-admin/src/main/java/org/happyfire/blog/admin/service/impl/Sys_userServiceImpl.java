package org.happyfire.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.happyfire.blog.admin.mapper.Sys_userMapper;
import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.pojo.Sys_user;
import org.happyfire.blog.admin.service.SecurityUserService;
import org.happyfire.blog.admin.service.Sys_userService;
import org.happyfire.blog.admin.vo.PageResult;
import org.happyfire.blog.admin.vo.Result;
import org.happyfire.blog.dao.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Sys_userServiceImpl implements Sys_userService {

    @Autowired
    private Sys_userMapper sys_userMapper;

    @Autowired
    private SecurityUserService securityUserService;

    private static final String slat = "happyfire";

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

    @Override
    public List<Sys_user> findUserBynickName(String nickName) {
        LambdaQueryWrapper<Sys_user> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Sys_user::getNickname, nickName);
        List<Sys_user> sysUserList = sys_userMapper.selectList(lambdaQueryWrapper);
        return sysUserList;
    }

    /**
     * 重置密码  重置后的密码为account（账号）
     * @param id
     * @return
     */
    @Override
    public Result clearPassword(Long id) {
        Sys_user sys_user = sys_userMapper.selectById(id);
        String password = sys_user.getAccount(); //重置后的密码为account（账号）
        //注册和登录时前端进行过一次md5加密 后端也进行次MD5加密
        //模拟前端加密
        password = DigestUtils.md5Hex(password);
        //模拟后端加密
        password = DigestUtils.md5Hex(password + slat);
        sys_user.setPassword(password);

        sys_userMapper.updateById(sys_user);
        return Result.success(null);
    }
}

