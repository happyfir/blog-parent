package org.happyfire.blog.service;

import org.happyfire.blog.dao.pojo.SysUser;
import org.happyfire.blog.vo.Result;
import org.happyfire.blog.vo.param.LoginParam;

public interface LoginService {
    /**
     * 登录功能
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    SysUser checkToken(String token);

    Result logout(String token);

    Result register(LoginParam registerParam);
}
