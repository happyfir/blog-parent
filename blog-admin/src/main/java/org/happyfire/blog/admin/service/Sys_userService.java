package org.happyfire.blog.admin.service;

import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.pojo.Sys_user;
import org.happyfire.blog.admin.vo.Result;

import java.util.List;

public interface Sys_userService {
    Result update(Sys_user sysUser);

    Result listUser(PageParam pageParam);

    Result delete(Long id);

//    Result getUserInfo();

    List<Sys_user> findUserBynickName(String condition);

    /**
     * 重置密码
     * @param id
     * @return
     */
    Result clearPassword(Long id);
}
