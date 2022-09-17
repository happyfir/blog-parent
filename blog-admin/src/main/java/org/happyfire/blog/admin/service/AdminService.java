package org.happyfire.blog.admin.service;

import org.happyfire.blog.admin.pojo.Admin;
import org.happyfire.blog.admin.pojo.Permission;

import java.util.List;

public interface AdminService {
    Admin findAdminByUsername(String username);

    List<Permission> findPermissionsByAdminId(Long id);
}
