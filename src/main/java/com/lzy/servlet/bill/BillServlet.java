package com.lzy.servlet.bill;

import com.alibaba.fastjson.JSONArray;
import com.lzy.pojo.Bill;
import com.lzy.pojo.Provider;
import com.lzy.pojo.User;
import com.lzy.service.Provider.ProviderService;
import com.lzy.service.Provider.ProviderServiceImpl;
import com.lzy.service.bill.BillService;
import com.lzy.service.bill.BillServiceImpl;
import com.lzy.util.Constants;
import com.lzy.util.PageSupport;
import com.mysql.cj.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author 李泽亿
 * @Date: 2023/10/26/ 10:51
 * @description
 */
public class BillServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method.equals("query") && method != null) {
            try {
                this.query(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (method.equals("add") && method != null) {
            try {
                this.add(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (method.equals("view") && method != null) {
            this.getBillById(req, resp, "billview.jsp");
        } else if (method != null && method.equals("modify")) {
            this.getBillById(req, resp, "billmodify.jsp");
        } else if (method.equals("modifysave") && method != null) {
            this.modify(req, resp);
        } else if (method.equals("delbill") && method != null) {
            this.delBill(req, resp);
        } else if (method.equals("getproviderlist") && method != null) {
            try {
                this.getProviderlist(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    public void query(HttpServletRequest req, HttpServletResponse resp) throws Exception, IOException {
        List<Provider> providerList = new ArrayList<>();
        ProviderService providerService = new ProviderServiceImpl();
        int pageSize = Constants.pageSize;
        int currentPageNo = 1;

        providerList = providerService.getProviderList("", "", currentPageNo, 10);
        req.setAttribute("providerList", providerList);

        String pageIndex = req.getParameter("pageIndex");
        String queryProductName = req.getParameter("queryProductName");
        String queryProviderId = req.getParameter("queryProviderId");
        String queryIsPayment = req.getParameter("queryIsPayment");

        if (StringUtils.isNullOrEmpty(queryProductName)) {
            queryProductName = "";
        }
        if (pageIndex != null) {
            try {
                currentPageNo = Integer.valueOf(pageIndex);
            } catch (NumberFormatException e) {
                resp.sendRedirect("error.jsp");
            }
        }

        List<Bill> billList = new ArrayList<>();
        BillServiceImpl billService = new BillServiceImpl();
        Bill bill = new Bill();


        if (StringUtils.isNullOrEmpty(queryIsPayment)) {
            bill.setIsPayment(0);
        } else {
            bill.setIsPayment(Integer.parseInt(queryIsPayment));
        }

        if (StringUtils.isNullOrEmpty(queryProviderId)) {
            bill.setProviderId(0);
        }else {
            bill.setProviderId(Integer.parseInt(queryProviderId));
        }
        bill.setProductName(queryProductName);

        //获取用户总数量（分页：存在上一页 下一页的情况）
        int totalCount = billService.getBillCount(bill);
        //总页数支持数
        PageSupport pages = new PageSupport();
        //设置当前页码
        pages.setCurrentPageNo(currentPageNo);
        //设置页总大小
        pages.setPageSize(pageSize);
        //设置页总数量
        pages.setTotalCount(totalCount);

        //控制首页和尾页
        int totalPageCount = pages.getTotalPageCount();

        if (currentPageNo < 1) { //显示第一页的东西
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) { //当前页面大于最后一页，让它成为最后一页
            currentPageNo = totalPageCount;
        }

        billList = billService.getBillList(bill, currentPageNo, pageSize);
        req.setAttribute("billList", billList);
        req.setAttribute("queryProductName", queryProductName);
        req.setAttribute("queryProviderId", queryProviderId);
        req.setAttribute("queryIsPayment", queryIsPayment);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currentPageNo);
        req.getRequestDispatcher("billlist.jsp").forward(req, resp);
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String billCode = req.getParameter("billCode");
        String productName = req.getParameter("productName");
        String productDesc = req.getParameter("productDesc");
        String productUnit = req.getParameter("productUnit");
        String productCount = req.getParameter("productCount");
        String totalPrice = req.getParameter("totalPrice");
        String providerId = req.getParameter("providerId");
        String isPayment = req.getParameter("isPayment");

        Bill bill = new Bill();
        bill.setBillCode(billCode);
        bill.setProductName(productName);
        bill.setProductDesc(productDesc);
        bill.setProductUnit(productUnit);
        bill.setProductCount(new BigDecimal(productCount).setScale(2, BigDecimal.ROUND_DOWN));
        bill.setIsPayment(Integer.parseInt(isPayment));
        bill.setTotalPrice(new BigDecimal(totalPrice).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setProviderId(Integer.parseInt(providerId));
        bill.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        bill.setCreationDate(new Date());
        boolean flag = false;
        BillServiceImpl billService = new BillServiceImpl();
        flag = billService.add(bill);
        System.out.println("add flag -- > " + flag);
        if (flag) {
            resp.sendRedirect(req.getContextPath() + "/jsp/bill.do?method=query");
        }else {
            req.getRequestDispatcher("billadd.jsp").forward(req, resp);
        }
    }

    private void getBillById(HttpServletRequest req, HttpServletResponse resp, String url) throws ServletException, IOException {
        String id = req.getParameter("billid");
        if (!StringUtils.isNullOrEmpty(id)) {
            BillServiceImpl billService = new BillServiceImpl();
            Bill bill = null;
            bill = billService.getBillById(id);
            req.setAttribute("bill", bill);
            req.getRequestDispatcher(url).forward(req, resp);
        }
    }

    private void modify(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        System.out.println("modify===============");
        String id = req.getParameter("id");
        String productName = req.getParameter("productName");
        String productDesc = req.getParameter("productDesc");
        String productUnit = req.getParameter("productUnit");
        String productCount = req.getParameter("productCount");
        String totalPrice = req.getParameter("totalPrice");
        String providerId = req.getParameter("providerId");
        String isPayment = req.getParameter("isPayment");

        Bill bill = new Bill();
        bill.setId(Integer.valueOf(id));
        bill.setProductName(productName);
        bill.setProductDesc(productDesc);
        bill.setProductUnit(productUnit);
        bill.setProductCount(new BigDecimal(productCount).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setIsPayment(Integer.parseInt(isPayment));
        bill.setTotalPrice(new BigDecimal(totalPrice).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setProviderId(Integer.parseInt(providerId));

        bill.setModifyBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        bill.setModifyDate(new Date());
        boolean flag = false;
        BillService billService = new BillServiceImpl();
        flag = billService.modify(bill);
        if(flag){
            resp.sendRedirect(req.getContextPath()+"/jsp/bill.do?method=query");
        }else{
            req.getRequestDispatcher("billmodify.jsp").forward(req, resp);
        }
    }

    private void delBill(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String id = req.getParameter("billid");
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(!StringUtils.isNullOrEmpty(id)){
            BillService billService = new BillServiceImpl();
            boolean flag = billService.deleteBillById(id);
            if(flag){//删除成功
                resultMap.put("delResult", "true");
            }else{//删除失败
                resultMap.put("delResult", "false");
            }
        }else{
            resultMap.put("delResult", "notexit");
        }
        //把resultMap转换成json对象输出
        resp.setContentType("application/json");
        PrintWriter outPrintWriter = resp.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    private void getProviderlist(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        System.out.println("getproviderlist =====================");

        List<Provider> providerList = new ArrayList<Provider>();
        ProviderService providerService = new ProviderServiceImpl();
        providerList = providerService.getProviderList("","", 1, 10);
        //把providerList转换成json对象输出
        resp.setContentType("application/json");
        PrintWriter outPrintWriter = resp.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(providerList));
        outPrintWriter.flush();
        outPrintWriter.close();
    }
}
