package com.demo09.dao;

import com.demo09.entity.User;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserDao {

    /**
     * 用户注册
     */
    public void register(User user);

    /**
     * 用户登录
     */
    public User login(String user_name);

    /**
     * 查询可用客户端
     */
    public List<User> findAvailable();

    /**
     * 修改为可用状态
     */
    public void Available(String user_id);

    /**
     * 修改为不可用状态
     */
    public void UnAvailable(String user_id);

    public List<User> findUsers();
}
