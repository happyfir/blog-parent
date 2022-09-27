package org.happyfire.blog.admin.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class Sys_user {
    @JsonSerialize(using = ToStringSerializer.class)
    Long id;
    String account;
    Integer admin;
//    String avatar;
//    Long create_date;
//    Boolean deleted;
    String email;
//    Long last_login;
//    @TableField("mobile_phone_number")
    String mobilePhoneNumber;
    String nickname;
    String password;
//    String salt;
//    String status;
}
