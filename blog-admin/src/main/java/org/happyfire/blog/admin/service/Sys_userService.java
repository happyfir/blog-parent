package org.happyfire.blog.admin.service;

import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.pojo.Sys_user;
import org.happyfire.blog.admin.vo.Result;

public interface Sys_userService {
    Result update(Sys_user sysUser);

    Result listUser(PageParam pageParam);

    Result delete(Long id);

    Result getUserInfo();
}
