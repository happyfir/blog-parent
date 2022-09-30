package org.happyfire.blog.service.impl;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.happyfire.blog.dao.pojo.SysUser;
import org.happyfire.blog.service.LoginService;
import org.happyfire.blog.service.SysUserService;
import org.happyfire.blog.utils.JWTUtils;
import org.happyfire.blog.vo.ErrorCode;
import org.happyfire.blog.vo.Result;
import org.happyfire.blog.vo.param.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private String DefaultAvatar = "http://rhzmlesrf.hb-bkt.clouddn.com/a47c9c65-dbc2-4604-bbb1-e3937532185b.jpg";

//    private static final String slat = "mszlu!@#";
    private static final String slat = "happyfire";

    @Override
    public Result login(LoginParam loginParam) {
        //检查参数是否合法
        //查询用户是否存在
        //不存在 登陆失败
        //存在 使用 jwt 生成token 返回前端
        //token 放入redis中 redis token ：user信息 设置过期时间(登录认证的时候，先认证token是否合法，去redis认证是否合法)
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
//        password = DigestUtils.md5DigestAsHex((password + slat).getBytes());
        password = DigestUtils.md5Hex(password + slat);
        SysUser sysUser = sysUserService.findUser(account,password);
        if (sysUser == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
        if (StringUtils.isBlank(token)){
            return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap == null){
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJson)){
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);

        return sysUser;
    }

    /**
     * 退出登录
     * @param token
     * @return
     */
    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_" + token);
        return Result.success(null);
    }

    /**
     * 注册
     * @param registerParam
     * @return
     */
    @Override
    public Result register(LoginParam registerParam) {
        //判断参数是否合法
        //判断账户是否存在 存在 返回账户已经被注册
        //注册用户 生成token 存入redis 并返回
        //架上事务管理
        String account = registerParam.getAccount();
        String password = registerParam.getPassword();
        String nickname = registerParam.getNickname();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password) || StringUtils.isBlank(nickname)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser sysUser = sysUserService.findUByAccount(account);
        if (sysUser != null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+slat));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar(DefaultAvatar);
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        this.sysUserService.save(sysUser);
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }
}
