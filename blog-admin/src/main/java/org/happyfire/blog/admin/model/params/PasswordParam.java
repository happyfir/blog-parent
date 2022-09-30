package org.happyfire.blog.admin.model.params;

import lombok.Data;

@Data
public class PasswordParam {
    private String username;
    private String password;
    private String newPassword;
    private String rePassword;
}
