package com.demo09.controller;

import com.alibaba.fastjson.JSONObject;
import com.demo09.entity.User;
import com.demo09.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 用于注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public JSONObject register(@RequestBody User user){
        JSONObject res = new JSONObject();
        String status = "", message = "";

        try {
            Map<String, String> map = userService.register(user);
            status = map.get("status");
            message = map.get("message");
        } catch (Exception e){
            e.printStackTrace();
            status = "202";
            message = "注册失败";
        }

        res.put("status",status);
        res.put("message",message);

        return res;
    }

    /**
     * 用于登录验证
     * @param user
     * @return
     */
    @PostMapping("/login")
    public JSONObject login(@RequestBody User user){

        System.out.println(user);

        JSONObject res = new JSONObject();
        String status = "", message = "", user_id = "", role = "", user_name = "";

        try {
            Map<String,String> map = userService.login(user.getUser_name(), user.getPassword());
            if (map.get("status").equals("1")){
                status = "200";
                message = "登录成功";
                user_id = map.get("user_id");
                role = map.get("role");
                user_name = map.get("user_name");
            }
            if (map.get("status").equals("2")){
                status = "201";
                message = "密码错误";
            }
            if (map.get("status").equals("3")){
                status = "202";
                message = "用户不存在";
            }
        } catch (Exception e){
            e.printStackTrace();
            status = "404";
            message = "登录失败";
        }

        res.put("status",status);
        res.put("message",message);
        res.put("user_id",user_id);
        res.put("role",role);
        res.put("user_name",user_name);

        return res;

    }

    /**
     * 用于安全退出
     * @param user
     * @return
     */
    @PostMapping("/logout")
    public JSONObject logout(@RequestBody User user){
        JSONObject res = new JSONObject();
        String status = "202", message = "安全退出失败！";

        try {
            userService.logout(user);
            status = "200";
            message = "安全退出成功";
        } catch (Exception e){
            e.printStackTrace();
        }

        res.put("status",status);
        res.put("message",message);

        return res;
    }
}
