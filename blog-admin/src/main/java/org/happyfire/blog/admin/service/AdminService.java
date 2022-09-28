package org.happyfire.blog.admin.service;

import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.pojo.Admin;
import org.happyfire.blog.admin.pojo.Permission;
import org.happyfire.blog.admin.vo.AdminVo;
import org.happyfire.blog.admin.vo.Result;

import java.util.List;

public interface AdminService {
    Admin findAdminByUsername(String username);

    List<Permission> findPermissionsByAdminId(Long id);

    Result getAdmins(PageParam pageParam);

    Result update(AdminVo adminVo);

    Result deleteById(Long id);

    Result addAdmin(AdminVo adminVo);

    Result clearPassword(Long id);
}
