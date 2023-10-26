package com.lzy.service.role;

import com.lzy.pojo.Role;

import java.sql.SQLException;
import java.util.List;

/**
 * @Author 李泽亿
 * @Date: 2023/10/24/ 20:19
 * @description
 */
public interface RoleService {
    //获取角色列表
    public List<Role> getRoleList() throws SQLException;

}
