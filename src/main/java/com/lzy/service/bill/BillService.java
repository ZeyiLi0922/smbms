package com.lzy.service.bill;

import com.lzy.pojo.Bill;

import java.sql.SQLException;
import java.util.List;

/**
 * @Author 李泽亿
 * @Date: 2023/10/26/ 0:17
 * @description
 */
public interface BillService {
    //增加订单
    public boolean add(Bill bill) throws Exception;

    //查询订单数量
    public int getBillCount(Bill bill) throws Exception;

    //通过条件获取订单列表-模糊查询-billList
    public List<Bill> getBillList(Bill bill, int currentPageNo, int pageSize);

    //通过billId删除Bill
    public boolean deleteBillById(String delId);

    //通过billId获取Bill
    public Bill getBillById(String id);

    //修改订单信息
    public boolean modify(Bill bill);
}
