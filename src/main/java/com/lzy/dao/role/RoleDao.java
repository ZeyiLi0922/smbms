package com.lzy.dao.role;

import com.lzy.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author 李泽亿
 * @Date: 2023/10/24/ 20:10
 * @description
 */
public interface RoleDao {
    //获取角色列表
    public List<Role> getRoleList(Connection connection) throws SQLException;
}
