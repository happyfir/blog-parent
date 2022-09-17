package org.happyfire.blog.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.happyfire.blog.admin.pojo.Admin;
import org.happyfire.blog.admin.pojo.Permission;

import java.util.List;

@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

    @Select("select * from ms_permission where id in (select permission_id from ms_admin_permission where admin_id=#{id})")
    List<Permission> findPermissionsByAdminId(Long id);
}
