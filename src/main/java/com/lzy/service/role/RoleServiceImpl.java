package com.lzy.service.role;

import com.lzy.dao.BaseDao;
import com.lzy.dao.role.RoleDao;
import com.lzy.dao.role.RoleDaoImpl;
import com.lzy.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author 李泽亿
 * @Date: 2023/10/24/ 20:21
 * @description
 */
public class RoleServiceImpl implements RoleService{
    //引入Dao
    private RoleDao roleDao;

    public RoleServiceImpl() {
        roleDao = new RoleDaoImpl();
    }

    @Override
    public List<Role> getRoleList() throws SQLException {

        Connection connection = null;
        List<Role> roleList = null;
        connection = BaseDao.getConnection();
        roleList = roleDao.getRoleList(connection);

        BaseDao.closeResource(connection, null, null);
        return roleList;
    }
}
