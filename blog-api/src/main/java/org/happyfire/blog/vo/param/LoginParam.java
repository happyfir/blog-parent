package org.happyfire.blog.vo.param;

import lombok.Data;

@Data
public class LoginParam {
    private String account;
    private String password;
    private String nickname;
//    private String repassword;
}
