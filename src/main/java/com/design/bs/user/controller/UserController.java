package com.design.bs.user.controller;

import com.design.bs.core.dto.QueryPageRequest;
import com.design.bs.core.dto.QueryPageResult;
import com.design.bs.core.dto.Response;
import com.design.bs.core.enums.StatusCode;
import com.design.bs.core.exception.BusinessException;
import com.design.bs.core.utils.PageUtils;
import com.design.bs.core.utils.PasswordHelper;
import com.design.bs.core.utils.UserUtils;
import com.design.bs.user.entity.User;
import com.design.bs.user.mapper.UserMapper;
import com.design.bs.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 用户服务
 **/
@RestController
@Slf4j
public class UserController {

	@Autowired
    private UserMapper userMapper;
	
    @Autowired
    private IUserService userService;
    
    @Autowired
    private PasswordHelper passwordHelper;

    @GetMapping("/user/info")
    public User info() {
    	return UserUtils.getCurrentUser();
    }

    @GetMapping("/user")
    public QueryPageResult list(@RequestParam("pageNum") int pageNum,@RequestParam("pageSize") int pageSize,@RequestParam("keyword") String keyword){
        QueryPageRequest queryPageRequest = new QueryPageRequest();
        queryPageRequest.setPage(pageNum);
        queryPageRequest.setRows(pageSize);
        User user = new User();
        user.setUsercode(keyword);
        user.setName(keyword);
        QueryPageResult queryPageResult = PageUtils.queryPage(queryPageRequest, () -> userService.queryList(user));
        queryPageResult.setPageNum(pageNum);
        queryPageResult.setPageSize(pageSize);
        return queryPageResult;
    }

    @PostMapping("/user")
    public Boolean add(@RequestBody User user) {
        //监测用户名是否重复
        if(userService.checkUsername(user.getUsercode())) {
            user.setStatus(1);
            user.setCreateTime(new Date());
            passwordHelper.encryptPassword(user);
            user.setCreateUser(user.getCreateUser() == null ? UserUtils.getCurrentUser().getId() : user.getCreateUser());
            userMapper.insertSelective(user);
        }else{
            throw new BusinessException(StatusCode.SYSTEM_ERROR,"账号已存在");
        }
        return true;
    }

    @PutMapping("/user")
    public Boolean update(@RequestBody User user) {
        //更新用户信息
        user.setPassword(null); //这里设置为null，结合 updateNotNull 方法，表示不更新密码
        user.setUpdateTime(new Date());
        user.setSalt(null);
        user.setUpdateUser(UserUtils.getCurrentUser().getId());
        userService.updateNotNull(user);
        return true;
    }

    @DeleteMapping("/user/{id}")
    public Boolean delete(@PathVariable Long id) {
        userService.delete(Arrays.asList(id));
        return true;
    }

    @GetMapping("/user/{id}")
    public User find(@PathVariable Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @PostMapping("/checkUsername")
    public Boolean checkName(@RequestBody Map<String,String> map) {
        return userService.checkUsername(map.get("usercode"));
    }
    
    @PostMapping("/checkPassword")
    public Boolean checkPassword(@RequestBody User user) {
    	return userService.checkPassword(user.getPassword());
    }


    @PostMapping("/changeAvatar")
    public Boolean changeAvatar(String url) {
        User user = UserUtils.getCurrentUser();
        user.setAvatar(url);
        userService.updateNotNull(user);
        return true;
    }

    @PostMapping("/updatePassword")
    public Boolean updatePassword(@RequestBody User user) {
        userService.updatePassword(user);
        return true;
    }

    @PostMapping("/modifyPassword")
    public Response modifyPassword(User user){
        userService.updatePassword(user);
        return new Response(StatusCode.OK);
    }
    
    @PostMapping("/resetPassword/{userId}")
    public Boolean resetPassword(@PathVariable Long userId) {
    	String password = userService.resetPassword(userId);
    	//异步发送邮件
    	userService.sendMail(userId,password);
    	return true;
    }

    @PostMapping("/user/upload")
    public Map<String,String> uplaod(HttpServletRequest req, @RequestParam("file") MultipartFile file) {
        Map map=new HashMap<String,String>();
        map.put("status_code","200");
        String destFileName = "";
        String fileName="";
        try {
            //根据创建时间对文件进行重命名
            fileName = System.currentTimeMillis() + "."+ file.getOriginalFilename().split("\\.")[1];
            //上传文件存储的位置
            destFileName = "C:\\upload\\" + fileName;
            //防止改文件夹不存在，创建一个新文件夹
            File destFile = new File(destFileName);
            destFile.getParentFile().mkdirs();
            //将文件存储到该位置
            IOUtils.copy(file.getInputStream(),new FileOutputStream(destFile));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        map.put("avatar",fileName);
        return map;
    }

    @GetMapping("/user/preview/{avatar}")
    public void readImage(@PathVariable("avatar") String avatar, HttpServletResponse response){
        String destFileName = "";
        InputStream fis = null;
        try {
            //上传文件存储的位置
            destFileName = "C:\\upload\\" + avatar;

            File destFile = new File(destFileName);

            fis = new FileInputStream(destFile);

            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(avatar+"", "UTF-8"));
            ServletOutputStream outputStream = response.getOutputStream();

            IOUtils.copy(fis,outputStream);

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @GetMapping("/user/download/{id}")
    public void readImage(@PathVariable("id") Long id, HttpServletResponse response){
        String destFileName = "";
        InputStream fis = null;
        try {
            User user = userMapper.selectByPrimaryKey(id);
            //上传文件存储的位置
            destFileName = "C:\\upload\\" + user.getAvatar();

            File destFile = new File(destFileName);

            fis = new FileInputStream(destFile);

            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(user.getName()+"", "UTF-8"));
            ServletOutputStream outputStream = response.getOutputStream();

            IOUtils.copy(fis,outputStream);

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
