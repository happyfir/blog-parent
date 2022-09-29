package org.happyfire.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.happyfire.blog.admin.mapper.AdminMapper;
import org.happyfire.blog.admin.mapper.AdminPermissionMapper;
import org.happyfire.blog.admin.mapper.PermissionMapper;
import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.pojo.Admin;
import org.happyfire.blog.admin.pojo.AdminPermission;
import org.happyfire.blog.admin.pojo.Permission;
import org.happyfire.blog.admin.service.AdminService;
import org.happyfire.blog.admin.vo.AdminVo;
import org.happyfire.blog.admin.vo.PageResult;
import org.happyfire.blog.admin.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements AdminService{

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private AdminPermissionMapper adminPermissionMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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

    /**
     * 根据查询条件 查找用户列表
     * @param pageParam
     * @return
     */
    //TODO  完善用户权限管理
    @Override
    public Result getAdmins(PageParam pageParam) {
        Page<Admin> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
        LambdaQueryWrapper<Admin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String condition = pageParam.getQueryString();
        if (StringUtils.isNoneBlank(condition)){
            lambdaQueryWrapper.like(Admin::getUsername,condition);
        }
        Page<Admin> adminPage = adminMapper.selectPage(page, lambdaQueryWrapper);
        List<Admin> adminRecords = adminPage.getRecords();
        List<AdminVo> adminVoList = copyList(adminRecords);

        PageResult<AdminVo> pageResult = new PageResult<>();
        pageResult.setList(adminVoList);
        pageResult.setTotal(adminPage.getTotal());
        return Result.success(pageResult);
    }

    /**
     * 更新用户  实际上只能修改用户的权限 permissionId
     * @param adminVo
     * @return
     */
    @Override
    public Result update(AdminVo adminVo) {
        Long permissionId = adminVo.getPermissionId();
        Long adminId = adminVo.getId();
        LambdaQueryWrapper<AdminPermission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AdminPermission::getAdminId,adminId);
        AdminPermission adminPermission = adminPermissionMapper.selectOne(lambdaQueryWrapper);
        adminPermission.setPermissionId(permissionId);
        adminPermissionMapper.updateById(adminPermission);
        return Result.success(null);
    }

    @Override
    public Result deleteById(Long id) {
        Admin admin = adminMapper.selectById(id);
        //获取对应的权限用户对象
        LambdaQueryWrapper<AdminPermission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AdminPermission::getAdminId,admin.getId());
        adminPermissionMapper.delete(lambdaQueryWrapper);
        adminMapper.deleteById(admin.getId());
        return Result.success(null);
    }

    /**
     * 添加用户
     * @param adminVo
     * @return
     */
    @Override
    public Result addAdmin(AdminVo adminVo) {
        //账户是唯一的
        LambdaQueryWrapper<Admin> adminLambdaQueryWrapper = new LambdaQueryWrapper<>();
        adminLambdaQueryWrapper.eq(Admin::getUsername,adminVo.getUsername());
        Admin admin = adminMapper.selectOne(adminLambdaQueryWrapper);
        if (admin != null){
            return Result.fail(-1,"该用户已存在");
        }
        Admin newAdmin = new Admin();
        BeanUtils.copyProperties(adminVo,newAdmin);
        //设置密码
        newAdmin.setPassword(passwordEncoder.encode(newAdmin.getPassword()));
        //插入用户
        adminMapper.insert(newAdmin);
        //再次查找获取插入的用户id
        LambdaQueryWrapper<Admin> adminWrapper = new LambdaQueryWrapper<>();
        adminWrapper.eq(Admin::getUsername,newAdmin.getUsername());
        newAdmin = adminMapper.selectOne(adminWrapper);
        //设置用户与权限
        AdminPermission adminPermission = new AdminPermission();
        adminPermission.setPermissionId(adminVo.getPermissionId());
        adminPermission.setAdminId(newAdmin.getId());
        adminPermissionMapper.insert(adminPermission);
        return Result.success(null);
    }

    /**
     * 重置密码  重置后的密码为username（账号）
     * @param id
     * @return
     */
    @Override
    public Result clearPassword(Long id) {
        Admin admin = adminMapper.selectById(id);
        String password = admin.getUsername();
        //注册和登录时前端进行过一次md5加密 后端也进行次MD5加密
        //模拟前端加密
        password = DigestUtils.md5Hex(password);
        //模拟后端加密
        String encode = passwordEncoder.encode(password);
        admin.setPassword(encode);
        adminMapper.updateById(admin);
        return Result.success(null);
    }

    private List<AdminVo> copyList(List<Admin> records){
        List<AdminVo> adminVoList = new ArrayList<>();
        for (Admin record : records) {
            adminVoList.add(copy(record));
        }
        return adminVoList;
    }

    private AdminVo copy(Admin admin){
        AdminVo adminVo = new AdminVo();
        BeanUtils.copyProperties(admin,adminVo);
        //查找用户对应的权限
        LambdaQueryWrapper<AdminPermission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AdminPermission::getAdminId,admin.getId());
        AdminPermission adminPermission = adminPermissionMapper.selectOne(lambdaQueryWrapper);
        Permission permission = permissionMapper.selectById(adminPermission.getPermissionId());
        adminVo.setPermissionId(permission.getId());
        adminVo.setPermissionName(permission.getName());
        return adminVo;
    }
}
