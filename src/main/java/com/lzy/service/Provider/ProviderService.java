package com.lzy.service.Provider;

import com.lzy.pojo.Provider;

import java.sql.SQLException;
import java.util.List;

/**
 * @Author 李泽亿
 * @Date: 2023/10/25/ 21:49
 * @description
 */
public interface ProviderService {
    //查询供应商数量
    public int getProviderCount(String proName, String proCode) throws Exception;
    //通过供应商名称、编码获取供应商列表-模糊查询-providerList
    public List<Provider> getProviderList(String proName, String proCode, int currentPageNo, int pageSize) throws Exception;

    //增加供应商
    public boolean add(Provider provider) throws Exception;

    //通过proId删除Provider
    public int deleteProviderById(String delId) throws Exception;

    //修改供应商信息
    public boolean modify(Provider provider) throws Exception;

    //通过proId获取Provider
    public Provider getProviderById(String id) throws Exception;
}
