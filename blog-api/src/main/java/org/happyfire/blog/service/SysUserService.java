package org.happyfire.blog.service;

import org.happyfire.blog.dao.pojo.SysUser;
import org.happyfire.blog.vo.Result;
import org.happyfire.blog.vo.UserVo;
import org.happyfire.blog.vo.param.ChangeParam;

public interface SysUserService {
    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    /**
     * 根据token查询用户信息
     * @param token
     * @return
     */
    Result findUserByToken(String token);

    /**
     * 根据账户查找用户
     * @param account
     * @return
     */
    SysUser findUByAccount(String account);

    /**
     * 保存用户
     * @param sysUser
     */
    void save(SysUser sysUser);

    UserVo findUserVoById(Long toUid);

    void update(SysUser sysUser);

    Result changeUserInfo(String token, ChangeParam changeParam);

    Result changePassword(ChangeParam changeParam);
}
