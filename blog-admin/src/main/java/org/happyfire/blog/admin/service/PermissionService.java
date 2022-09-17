package org.happyfire.blog.admin.service;

import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.pojo.Permission;
import org.happyfire.blog.admin.vo.Result;

public interface PermissionService {
    Result listPermission(PageParam pageParam);
    Result add(Permission permission);

    Result update(Permission permission);

    Result delete(Long id);
}
