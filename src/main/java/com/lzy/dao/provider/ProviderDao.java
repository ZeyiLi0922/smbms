package com.lzy.dao.provider;

import com.lzy.pojo.Provider;

import java.sql.Connection;
import java.util.List;

/**
 * @Author 李泽亿
 * @Date: 2023/10/25/ 10:44
 * @description
 */
public interface ProviderDao {
    //查询供应商的数量
    public int getProviderContent(Connection connection, String proName, String proCode) throws Exception;
    //通过供应商名称、编码获取供应商列表-模糊查询
    public List<Provider> getProviderList(Connection connection, String proName, String proCode, int currentPageNo, int pageSize) throws Exception;

    //增加供应商
    public int add(Connection connection, Provider provider) throws Exception;

    //通过proId删除Provider
    public int deleteProviderById(Connection connection, String delId) throws Exception;

    //修改供应商信息
    public int modify(Connection connection, Provider provider) throws Exception;

    //通过proId获取Provider
    public Provider getProviderById(Connection connection, String id) throws Exception;
}
