package com.lzy.dao.user;

import com.lzy.pojo.Role;
import com.lzy.pojo.User;

import javax.management.relation.RoleList;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author 李泽亿
 * @Date: 2023/10/23/ 21:03
 * @description
 */
public interface UserDao {
    //得到要登录的用户
    public User getLoginUser(Connection connection, String userCode) throws SQLException;

    //修改当前用户密码
    public int updatePwd(Connection connection, int id, String password) throws SQLException;

    //查询用户总数
    public int getUserContent(Connection connection, String username, int userRole) throws SQLException;

    //通过条件查询userList
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException;

    //增加用户信息
    public int add(Connection connection, User user) throws SQLException;

    //通过userId删除user
    public int deleteUserById(Connection connection, Integer delId) throws SQLException;

    //修改用户信息
    public int modify(Connection connection, User user) throws Exception;

    //通过userId获取user
    public User getUserById(Connection connection, String id) throws Exception;

}
