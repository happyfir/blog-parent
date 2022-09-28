package org.happyfire.blog.admin.vo;

import lombok.Data;

@Data
public class AdminVo {
    private Long id;

    private String username;

    private String password;

    private Long permissionId;
    /**
     * 权限名称
     */
    private String permissionName;
}
