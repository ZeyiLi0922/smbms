package com.lzy.service.Provider;

import com.lzy.dao.BaseDao;
import com.lzy.dao.bill.BillDao;
import com.lzy.dao.bill.BillDaoImpl;
import com.lzy.dao.provider.ProviderDao;
import com.lzy.dao.provider.ProviderDaoImpl;
import com.lzy.pojo.Bill;
import com.lzy.pojo.Provider;
import sun.plugin2.main.client.DisconnectedExecutionContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author 李泽亿
 * @Date: 2023/10/25/ 21:52
 * @description
 */
public class ProviderServiceImpl implements ProviderService {
    private ProviderDao providerDao;
    private BillDao billDao;
    public ProviderServiceImpl() {
        providerDao = new ProviderDaoImpl();
        billDao = new BillDaoImpl();
    }

    @Override
    public int getProviderCount(String proName, String proCode) throws Exception {
        Connection connection = null;
        int count = 0;
        connection = BaseDao.getConnection();
        count = providerDao.getProviderContent(connection, proName, proCode);

        BaseDao.closeResource(connection, null, null);

        return count;
    }

    @Override
    public List<Provider> getProviderList(String proName, String proCode, int currentPageNo, int pageSize) throws Exception {
        Connection connection = null;
        List<Provider> providerList = null;
        System.out.println("query proName ---- > " + proName);
        System.out.println("query proCode ---- > " + proCode);
        try {
            connection = BaseDao.getConnection();
            providerList = providerDao.getProviderList(connection, proName,proCode, currentPageNo, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return providerList;
    }

    @Override
    public boolean add(Provider provider) throws Exception {
        boolean flag = false;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false); //开启JDBC事务
            if (providerDao.add(connection, provider) > 0) {
                flag = true;
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        return flag;
    }

    @Override
    public int deleteProviderById(String delId) throws Exception {
        Connection connection = null;
        int billCount = -1;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            billCount = billDao.getBillCountByProviderId(connection, delId);
            if (billCount == 0) {
                providerDao.deleteProviderById(connection, delId);
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            billCount = -1;
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        return billCount;
    }

    @Override
    public boolean modify(Provider provider) throws Exception {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if (providerDao.modify(connection, provider) > 0) {
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
    public Provider getProviderById(String id) throws Exception {
        Provider provider = null;
        Connection connection = null;
        try{
            connection = BaseDao.getConnection();
            provider = providerDao.getProviderById(connection, id);
        } catch (Exception e) {
            e.printStackTrace();
            provider = null;
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return provider;
    }
}
