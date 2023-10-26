package com.lzy.service.bill;

import com.lzy.dao.BaseDao;
import com.lzy.dao.bill.BillDao;
import com.lzy.dao.bill.BillDaoImpl;
import com.lzy.pojo.Bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author 李泽亿
 * @Date: 2023/10/26/ 0:17
 * @description
 */
public class BillServiceImpl implements BillService {
    private BillDao billDao;
    public BillServiceImpl() {
        billDao = new BillDaoImpl();
    }
    @Override
    public boolean add(Bill bill) throws Exception {
        boolean flag = false;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            if (billDao.add(connection, bill) > 0) {
                flag = true;
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                System.out.println("rollback=================");
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
    public int getBillCount(Bill bill) throws Exception {
        Connection connection = null;
        int count = 0;
        connection = BaseDao.getConnection();
        count = billDao.getBillContent(connection, bill);

        BaseDao.closeResource(connection, null, null);

        return count;
    }

    @Override
    public List<Bill> getBillList(Bill bill, int currentPageNo, int pageSize) {
        Connection connection = null;
        List<Bill> billList = null;
        System.out.println("query productName ---- > " + bill.getProductName());
        System.out.println("query providerId ---- > " + bill.getProviderId());
        System.out.println("query isPayment ---- > " + bill.getIsPayment());

        try {
            connection = BaseDao.getConnection();
            billList = billDao.getBillList(connection, bill, currentPageNo, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return billList;
    }

    @Override
    public boolean deleteBillById(String delId) {
        // TODO Auto-generated method stub
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if(billDao.deleteBillById(connection, delId) > 0)
                flag = true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            BaseDao.closeResource(connection, null, null);
        }
        return flag;
    }

    @Override
    public Bill getBillById(String id) {
        Bill bill = null;
        Connection connection = null;
        try{
            connection = BaseDao.getConnection();
            bill = billDao.getBillById(connection, id);
        }catch (Exception e) {
            e.printStackTrace();
            bill = null;
        }finally{
            BaseDao.closeResource(connection, null, null);
        }
        return bill;
    }

    @Override
    public boolean modify(Bill bill) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if(billDao.modify(connection,bill) > 0)
                flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            BaseDao.closeResource(connection, null, null);
        }
        return flag;
    }
}
