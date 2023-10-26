package com.lzy.dao.bill;

import com.lzy.pojo.Bill;

import java.security.PublicKey;
import java.sql.Connection;
import java.util.List;

/**
 * @Author 李泽亿
 * @Date: 2023/10/25/ 23:27
 * @description
 */
public interface BillDao {
    //增加订单
    public int add(Connection connection, Bill bill) throws Exception;

    //查询订单的数量
    public int getBillContent(Connection connection, Bill bill) throws Exception;

    //通过查询条件获取供应商列表-模糊查询
    public List<Bill> getBillList(Connection connection, Bill bill, int currentPageNo, int pageSize) throws Exception;

    //通过delId删除Bill
    public int deleteBillById(Connection connection, String delId) throws Exception;

    //通过billId获取Bill
    public Bill getBillById(Connection connection, String id) throws  Exception;

    //修改订单信息
    public int modify(Connection connection, Bill bill) throws Exception;

    //根据供应商ID查询订单数量
    public int getBillCountByProviderId(Connection connection, String providerId) throws Exception;
}
