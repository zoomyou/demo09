package com.demo09.service;

import com.demo09.dao.UserDao;
import com.demo09.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserService {

    @Autowired
    UserDao userDao;

    //注册
    public Map<String, String> register(User user){

        Map<String, String> map = new HashMap<>();
        String status="",message="";

        //用户状态为1时，表示当前为非空闲状态
        user.setStatus("1");

        //1.先判断用户名是否重复
        User judge = userDao.login(user.getUser_name());
        if (ObjectUtils.isEmpty(judge)){
            //2.如果为空则可以注册
            userDao.register(user);
            status = "200";
            message = "注册成功";
        } else {
            //3.如果不为空则说明用户名重复重新注册
            status = "201";
            message = "用户名已存在";
            throw new RuntimeException("用户名已存在");
        }

        map.put("status",status);
        map.put("message",message);

        return map;
    }

    //登录
    public Map<String,String> login(String user_name, String password){

        Map<String,String> map = new HashMap<>();
        String status = "",user_id = "",role = "",user_name1="";

        //1.先判断用户是否存在
        User judge = userDao.login(user_name);
        if (!ObjectUtils.isEmpty(judge)){
            //2.如果用户存在则进行密码的对比
            if (judge.getPassword().equals(password)){
                status = "1";
                user_id = judge.getUser_id();
                role = judge.getRole();
                user_name1 = judge.getUser_name();
                //将客户端设置为可用状态
                userDao.Available(judge.getUser_id());
            } else {
                status = "2";
            }
        } else {
            status = "3";
            throw new RuntimeException("用户不存在");
        }

        map.put("status",status);
        map.put("user_id",user_id);
        map.put("role",role);
        map.put("user_name",user_name1);

        return map;
    }

    //安全退出
    public void logout(User user){
        //安全退出即为要将状态修改为不可用的状态
        userDao.UnAvailable(user.getUser_id());
    }
}
