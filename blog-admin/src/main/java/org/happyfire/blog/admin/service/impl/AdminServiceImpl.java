package org.happyfire.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.happyfire.blog.admin.mapper.AdminMapper;
import org.happyfire.blog.admin.mapper.AdminPermissionMapper;
import org.happyfire.blog.admin.mapper.PermissionMapper;
import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.model.params.PasswordParam;
import org.happyfire.blog.admin.pojo.Admin;
import org.happyfire.blog.admin.pojo.AdminPermission;
import org.happyfire.blog.admin.pojo.Permission;
import org.happyfire.blog.admin.service.AdminService;
import org.happyfire.blog.admin.vo.AdminVo;
import org.happyfire.blog.admin.vo.PageResult;
import org.happyfire.blog.admin.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
     * ?????????????????? ??????????????????
     * @param pageParam
     * @return
     */
    //TODO  ????????????????????????
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
     * ????????????  ???????????????????????????????????? permissionId
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
        //?????????????????????????????????
        LambdaQueryWrapper<AdminPermission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AdminPermission::getAdminId,admin.getId());
        adminPermissionMapper.delete(lambdaQueryWrapper);
        adminMapper.deleteById(admin.getId());
        return Result.success(null);
    }

    /**
     * ????????????
     * @param adminVo
     * @return
     */
    @Override
    public Result addAdmin(AdminVo adminVo) {
        //??????????????????
        LambdaQueryWrapper<Admin> adminLambdaQueryWrapper = new LambdaQueryWrapper<>();
        adminLambdaQueryWrapper.eq(Admin::getUsername,adminVo.getUsername());
        Admin admin = adminMapper.selectOne(adminLambdaQueryWrapper);
        if (admin != null){
            return Result.fail(-1,"??????????????????");
        }
        Admin newAdmin = new Admin();
        BeanUtils.copyProperties(adminVo,newAdmin);
        //????????????
        newAdmin.setPassword(passwordEncoder.encode(newAdmin.getPassword()));
        //????????????
        adminMapper.insert(newAdmin);
        //?????????????????????????????????id
        LambdaQueryWrapper<Admin> adminWrapper = new LambdaQueryWrapper<>();
        adminWrapper.eq(Admin::getUsername,newAdmin.getUsername());
        newAdmin = adminMapper.selectOne(adminWrapper);
        //?????????????????????
        AdminPermission adminPermission = new AdminPermission();
        adminPermission.setPermissionId(adminVo.getPermissionId());
        adminPermission.setAdminId(newAdmin.getId());
        adminPermissionMapper.insert(adminPermission);
        return Result.success(null);
    }


    @Override
    public Result getAdminInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User)principal;
        return Result.success(user);
    }

    /**
     * ????????????
     * @param admin
     * @return
     */
    @Override
    public Result setPassword(Admin admin) {
        //TODO ???????????????????????????????????? ???????????????????????? ??????????????????
        String password = admin.getPassword();
        String encode = passwordEncoder.encode(password);
        admin.setPassword(encode);
        adminMapper.updateById(admin);
        return Result.success(null);
    }

//    @Override
//    public Result changePassword(PasswordParam passwordParam) {
//        LambdaQueryWrapper<Admin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        String password = passwordEncoder.encode(passwordParam.getPassword());
//        lambdaQueryWrapper.eq(Admin::getUsername,passwordParam.getUsername());
//        Admin admin = adminMapper.selectOne(lambdaQueryWrapper);
//        System.out.println("0000000000000000");
//        System.out.println(admin);
////        if (admin == null || (!password.equals(admin.getPassword()))){
////            return Result.fail(-3,"??????????????????");
////        }
//        String newPassword = passwordEncoder.encode(passwordParam.getNewPassword());
//        admin.setPassword(newPassword);
//        System.out.println("8888888888888888888");
//        System.out.println(admin);
//        adminMapper.updateById(admin);
//        return Result.success(null);
//    }

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
        //???????????????????????????
        LambdaQueryWrapper<AdminPermission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AdminPermission::getAdminId,admin.getId());
        AdminPermission adminPermission = adminPermissionMapper.selectOne(lambdaQueryWrapper);
        Permission permission = permissionMapper.selectById(adminPermission.getPermissionId());
        adminVo.setPermissionId(permission.getId());
        adminVo.setPermissionName(permission.getName());
        return adminVo;
    }
}
