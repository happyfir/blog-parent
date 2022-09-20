package org.happyfire.blog.vo.param;

import lombok.Data;

@Data
public class ChangeParam {
    private String account;
    private String nickname;
    private String password; //新密码
    private String oldPassword; //旧密码
    private String avatar;
}
