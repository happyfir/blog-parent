package org.happyfire.blog.admin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class AdminPermission {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long adminId;
    private Long permissionId;
}
