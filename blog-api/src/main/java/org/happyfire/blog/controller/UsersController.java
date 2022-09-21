package org.happyfire.blog.controller;

import org.happyfire.blog.service.SysUserService;
import org.happyfire.blog.vo.Result;
import org.happyfire.blog.vo.param.ChangeParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("users")
public class UsersController {

    @Autowired
    private SysUserService sysUserService;

    @Value("${blog.path}")
    private String basePath;

    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token){
        return sysUserService.findUserByToken(token);
    }

    /**
     * 修改用户信息 未修改密码
     * @param changeParam
     * @return
     */
    @PostMapping("changeUserInfo")
    public Result changeUserInfo(@RequestHeader("Authorization") String token, @RequestBody ChangeParam changeParam){
        return sysUserService.changeUserInfo(token,changeParam);
    }

    /**
     * 修改用户密码 包含其他信息
     * @param changeParam
     * @return
     */
    @PostMapping("changePassword")
    public Result changPassword(@RequestBody ChangeParam changeParam){
        return sysUserService.changePassword(changeParam);
    }

    /**
     * 头像上传和下载
     * @return
     */
    //TODO  将图片和当前用户联系起来
    @PostMapping("avatar")
    public Result uploadAvatar(MultipartFile file) {
        //原始文件名
        String originalFileName = file.getOriginalFilename();
        //获取原始名称后缀
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        //使用UUID重新生成文件名，防止文件名重复造成文件覆盖
        String filename = UUID.randomUUID().toString() + suffix;
        //创建一个目录对象
        File dir = new File(basePath);
        //判断当前目录是否存在
        if (!dir.exists()){
            //目录不存在
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basePath + filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success(filename);
    }

    /**
     * 将图片传输回前端 文件下载
     */
    @GetMapping("/getAvatar")
    public void download(String name, HttpServletResponse response) {
        try {
            //输入流 读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //输出流 写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpg");

            int len = 0;
            byte[] bytes = new byte[1024];
           while((len = fileInputStream.read(bytes)) != -1){
               outputStream.write(bytes,0,len);
               outputStream.flush();
           }
           outputStream.close();
           fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
