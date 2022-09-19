package org.happyfire.blog.vo.param;

import lombok.Data;

@Data
public class ChangePasswordParam {
    private String account;
    private String nickname;
    private String password; //新密码
    private String oldPassword; //旧密码
}
