package org.happyfire.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.happyfire.blog.admin.mapper.AdminMapper;
import org.happyfire.blog.admin.pojo.Admin;
import org.happyfire.blog.admin.pojo.Permission;
import org.happyfire.blog.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{

    @Autowired
    private AdminMapper adminMapper;

     public Admin findAdminByUsername(String username){
         LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
         queryWrapper.eq(Admin::getUsername, username);
         queryWrapper.last("limit 1");
         Admin admin = adminMapper.selectOne(queryWrapper);
         return admin;
     }

    @Override
    public List<Permission> findPermissionsByAdminId(Long id) {
        return adminMapper.findPermissionsByAdminId(id);
    }
}
