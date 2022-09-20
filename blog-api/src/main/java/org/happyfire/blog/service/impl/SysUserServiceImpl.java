package org.happyfire.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.happyfire.blog.dao.mapper.SysUserMapper;
import org.happyfire.blog.dao.pojo.SysUser;
import org.happyfire.blog.service.LoginService;
import org.happyfire.blog.service.SysUserService;
import org.happyfire.blog.vo.ErrorCode;
import org.happyfire.blog.vo.LoginUserVo;
import org.happyfire.blog.vo.Result;
import org.happyfire.blog.vo.UserVo;
import org.happyfire.blog.vo.param.ChangeParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private LoginService loginService;

    private static final String slat = "happyfire";

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setNickname("匿名用户");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword,password);
        queryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        //token合法性校验 是否为空 解析是否成功， redis是否存在
        //如果校验失败 返回错误
        //如果成功，返回对应的结果 LoginUserVo
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setId(String.valueOf(sysUser.getId()));
        loginUserVo.setNickname(sysUser.getNickname());
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setAccount(sysUser.getAccount());
        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        queryWrapper.last("limit 1");
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        return sysUser;
    }

    @Override
    public void save(SysUser sysUser) {
        //id自动生成 默认生成分布式id 雪花算法
        sysUserMapper.insert(sysUser);
    }



    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            //TODO 加入图片
            sysUser.setAvatar("");
            sysUser.setNickname("happyfire");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        userVo.setId(String.valueOf(sysUser.getId()));
        return userVo;
    }

    @Override
    public void update(SysUser sysUser) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getAccount,sysUser.getAccount());
        sysUserMapper.update(sysUser,lambdaQueryWrapper);
    }

    /**
     * 修改用户信息 （使用token）
     * @param token
     * @param changeParam
     * @return
     */
    @Override
    public Result changeUserInfo(String token, ChangeParam changeParam) {
        //token合法性校验 是否为空 解析是否成功， redis是否存在
        //如果校验失败 返回错误
        //如果成功，返回对应的结果 LoginUserVo
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        String nickName = changeParam.getNickname();
        if (StringUtils.isBlank(nickName)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        sysUser.setNickname(nickName);
        this.update(sysUser);
        return Result.success(null);
    }

    @Override
    public Result changePassword(ChangeParam changeParam) {
        //根据用户名查找 旧密码
        String account = changeParam.getAccount();
        String nickName = changeParam.getNickname();
        String password = changeParam.getPassword();
        String oldPassword = changeParam.getOldPassword();
        oldPassword = DigestUtils.md5Hex(oldPassword  + slat);
        if (StringUtils.isBlank(account)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser sysUser = this.findUser(account,oldPassword);
        if (sysUser == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        password = DigestUtils.md5Hex(password  + slat);
        sysUser.setPassword(password);
        sysUser.setNickname(nickName);
        this.update(sysUser);
        return Result.success(null);
    }
}
