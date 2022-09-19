package org.happyfire.blog.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.happyfire.blog.dao.mapper.SysUserMapper;
import org.happyfire.blog.dao.pojo.SysUser;
import org.happyfire.blog.service.ChangeService;
import org.happyfire.blog.service.SysUserService;
import org.happyfire.blog.vo.ErrorCode;
import org.happyfire.blog.vo.Result;
import org.happyfire.blog.vo.param.ChangePasswordParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChangeServiceImpl implements ChangeService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserService sysUserService;
    private static final String slat = "happyfire";
    @Override
    public Result changePassword(ChangePasswordParam changePasswordParam) {
        //根据用户名查找 旧密码
        String account = changePasswordParam.getAccount();
        String password = changePasswordParam.getPassword();
        String oldPassword = changePasswordParam.getOldPassword();
        oldPassword = DigestUtils.md5Hex(oldPassword  + slat);
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password) || StringUtils.isBlank(oldPassword)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser sysUser = sysUserService.findUser(account,oldPassword);
        if (sysUser == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        password = DigestUtils.md5Hex(password  + slat);
        sysUser.setPassword(password);
        sysUserService.update(sysUser);
        return Result.success(null);
    }
}
