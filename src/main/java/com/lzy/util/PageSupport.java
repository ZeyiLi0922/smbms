package com.lzy.util;

/**
 * @Author 李泽亿
 * @Date: 2023/10/24/ 15:50
 * @description
 */
public class PageSupport {
    //当前页码-来自于用户输入
    private int currentPageNo = 1;

    //总数量（表）
    private int totalCount = 0;

    //页面容量
    private int pageSize = 0;

    //总页数-totalCount/pageSize（+1）
    private int totalPageCount = 1;

    public int getCurrentPageNo() {
        return currentPageNo;
    }
    //OOP三大特性：封装、集成、多态
    /*
    封装：属性私有、get/set、在set中限制一些不安全的限制

     */
    public void setCurrentPageNo(int currentPageNo) {
        if(currentPageNo > 0){
            this.currentPageNo = currentPageNo;
        }
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        if(totalCount > 0){
            this.totalCount = totalCount;
            //设置总页数
            this.setTotalPageCountByRs();
        }
    }
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if(pageSize > 0){
            this.pageSize = pageSize;
        }
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public void setTotalPageCountByRs(){
        if(this.totalCount % this.pageSize == 0){
                this.totalPageCount = this.totalCount / this.pageSize;
            }else if(this.totalCount % this.pageSize > 0){
                this.totalPageCount = this.totalCount / this.pageSize + 1;
            }else{
                this.totalPageCount = 0;
        }
    }

}
