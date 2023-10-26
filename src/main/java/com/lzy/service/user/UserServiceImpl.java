package com.lzy.service.user;

import com.lzy.dao.BaseDao;
import com.lzy.pojo.User;
import com.lzy.dao.user.UserDao;
import com.lzy.dao.user.UserDaoImpl;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

//import static com.lzy.dao.BaseDao.username;

/**
 * @Author 李泽亿
 * @Date: 2023/10/18/ 20:55
 * @description
 */
@SuppressWarnings("all")
public class UserServiceImpl implements UserService{
    //业务层都会调用dao层，所以我们要引入Dao层;
    private UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }
    @Override
    public User login(String userCode, String password) throws SQLException {
        Connection connection = null;
        User user = null;

        connection = BaseDao.getConnection();
        //通过业务层调用对应的具体的数据库操作
        user = userDao.getLoginUser(connection, userCode);

        BaseDao.closeResource(connection, null, null);

        return user;
    }

    @Override
    public boolean updatePwd(int id, String pwd) throws SQLException {
        Connection connection = null;
        boolean flag = false;
        connection = BaseDao.getConnection();
        if (userDao.updatePwd(connection, id, pwd) > 0) {
            flag = true;
        }
        BaseDao.closeResource(connection, null, null);
        return flag;
    }

    @Override
    public int getUserCount(String username, int userRole) throws SQLException {
        Connection connection = null;
        int count = 0;
        connection = BaseDao.getConnection();
        count = userDao.getUserContent(connection, username, userRole);

        BaseDao.closeResource(connection, null, null);

        return count;
    }

    // @Test
    // public void test() throws SQLException {
    //     UserServiceImpl userService = new UserServiceImpl();
    //     int userCount = userService.getUserCount(null, 2);
    //
    //     System.out.println(userCount);
    // }

    @Override
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) throws SQLException {
        Connection connection = null;
        List<User> userList = null;
        connection = BaseDao.getConnection();
        userList = userDao.getUserList(connection, queryUserName, queryUserRole, currentPageNo, pageSize);

        BaseDao.closeResource(connection, null, null);

        return userList;
    }

    @Override
    public boolean add(User user) throws SQLException {
        boolean flag = false;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false); //开启JDBC事务管理
            int updateRows = userDao.add(connection, user);
            connection.commit();
            if (updateRows > 0) {
                flag = true;
                System.out.println("add success!");
            } else {
                System.out.println("add failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                System.out.println("rollback=======================");
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        return flag;
    }

    @Override
    public boolean deleteUserById(Integer delId) throws SQLException {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if (userDao.deleteUserById(connection, delId) > 0) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        return flag;
    }

    @Override
    public boolean modify(User user) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if (userDao.modify(connection, user) > 0) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            BaseDao.closeResource(connection, null, null);
        }
        return flag;
    }

    @Override
    public User selectUserCodeExist(String userCode) throws SQLException {
        User user = null;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            user = userDao.getLoginUser(connection, userCode);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        return user;
    }

    @Override
    public User getUserById(String id) throws Exception {
        User user = null;
        Connection connection = null;
        try{
            connection = BaseDao.getConnection();
            user = userDao.getUserById(connection, id);
        } catch (Exception e) {
            e.printStackTrace();
            user = null;
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        return user;
    }
}
