package com.lzy.service.user;

import com.lzy.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author 李泽亿
 * @Date: 2023/10/18/ 20:54
 * @description
 */
public interface UserService {
    //用户登陆
    public User login(String userCode, String password) throws SQLException;

    //根据用户ID修改密码
    public boolean updatePwd(int id, String pwd) throws SQLException;

    //查询记录数
    public int getUserCount(String username, int userRole) throws SQLException;

    //根据条件查询用户列表
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) throws SQLException;

    //增加用户信息
    public boolean add(User user) throws SQLException;

    //根据ID删除user
    public boolean deleteUserById(Integer delId) throws SQLException;

    //修该用户信息
    public boolean modify(User user);

    //根据userCode查询出User
    public User selectUserCodeExist(String userCode) throws SQLException;

    //根据ID查找user
    public User getUserById(String id) throws Exception;

}
