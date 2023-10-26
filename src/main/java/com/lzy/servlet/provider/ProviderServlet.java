package com.lzy.servlet.provider;

import com.alibaba.fastjson.JSONArray;
import com.lzy.pojo.Provider;
import com.lzy.pojo.User;
import com.lzy.service.Provider.ProviderService;
import com.lzy.service.Provider.ProviderServiceImpl;
import com.lzy.util.Constants;
import com.lzy.util.PageSupport;
import com.mysql.cj.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author 李泽亿
 * @Date: 2023/10/26/ 0:29
 * @description
 */
public class ProviderServlet extends HttpServlet {
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
        } else if (method.equals("delprovider") && method != null) {
            try {
                this.delProvider(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (method.equals("modifysave") && method != null) {
            try {
                this.modify(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (method.equals("view") && method != null) {
            try {
                this.getProviderById(req, resp, "providerview.jsp");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (method.equals("modify") && method != null) {
            try {
                this.getProviderById(req, resp, "providermodify.jsp");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void query(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String queryProName = req.getParameter("queryProName");
        String queryProCode = req.getParameter("queryProCode");
        String pageIndex = req.getParameter("pageIndex");

        int pageSize = Constants.pageSize;
        //当前页码
        int currentPageNo = 1;

        if (StringUtils.isNullOrEmpty(queryProName)) {
            queryProName = "";
        }
        if (StringUtils.isNullOrEmpty(queryProCode)) {
            queryProCode = "";
        }
        if (pageIndex != null) {
            try {
                currentPageNo = Integer.valueOf(pageIndex);
            } catch (NumberFormatException e) {
                resp.sendRedirect("error.jsp");
            }
        }
        ProviderServiceImpl providerService = new ProviderServiceImpl();

        //获取用户总数量（分页：存在上一页 下一页的情况）
        int totalCount = providerService.getProviderCount(queryProName, queryProCode);
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
        List<Provider> providerList = new ArrayList<>();
        providerList = providerService.getProviderList(queryProName, queryProCode, currentPageNo, pageSize);
        req.setAttribute("providerList", providerList);
        req.setAttribute("queryProName", queryProName);
        req.setAttribute("queryProCode", queryProCode);
        req.setAttribute("queryUserName", queryProName);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currentPageNo);
        req.getRequestDispatcher("providerlist.jsp").forward(req, resp);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String proCode = request.getParameter("proCode");
        String proName = request.getParameter("proName");
        String proContact = request.getParameter("proContact");
        String proPhone = request.getParameter("proPhone");
        String proAddress = request.getParameter("proAddress");
        String proFax = request.getParameter("proFax");
        String proDesc = request.getParameter("proDesc");

        Provider provider = new Provider();
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProFax(proFax);
        provider.setProAddress(proAddress);
        provider.setProDesc(proDesc);
        provider.setCreatedBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        provider.setCreationDate(new Date());
        boolean flag = false;
        ProviderService providerService = new ProviderServiceImpl();
        flag = providerService.add(provider);
        if(flag){
            response.sendRedirect(request.getContextPath()+"/jsp/provider.do?method=query");
        }else{
            request.getRequestDispatcher("provideradd.jsp").forward(request, response);
        }
    }

    private void delProvider(HttpServletRequest req, HttpServletResponse resp) throws Exception, IOException {
        String id = req.getParameter("proid");
        HashMap<String, String> resultMap = new HashMap<>();
        if (!StringUtils.isNullOrEmpty(id)) {
            ProviderService providerService = new ProviderServiceImpl();
            int flag = providerService.deleteProviderById(id);
            if (flag == 0){
                resultMap.put("delResult", "true");
            } else if (flag == -1) {
                resultMap.put("delResult", "false");
            } else if (flag >= 1) { //供应商有下订单，不能删除
                resultMap.put("delResult", String.valueOf(flag));
            }
        } else {
            resultMap.put("delResult", "notexist");
        }
        //把resultMap转成json对象输出
        resp.setContentType("application/json");
        PrintWriter outPrintWriter = resp.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    private void modify(HttpServletRequest req, HttpServletResponse resp) throws Exception, IOException {
        String proContact = req.getParameter("proContact");
        String proName = req.getParameter("proName");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");
        String id = req.getParameter("id");

        Provider provider = new Provider();
        provider.setProContact(proContact);
        provider.setProName(proName);
        provider.setProPhone(proPhone);
        provider.setProAddress(proAddress);
        provider.setProFax(proFax);
        provider.setProDesc(proDesc);
        provider.setId(Integer.valueOf(id));
        provider.setModifyBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        provider.setModifyDate(new Date());

        boolean flag = false;
        ProviderServiceImpl providerService = new ProviderServiceImpl();
        flag = providerService.modify(provider);
        System.out.println("-------------------> " + flag);
        if (flag) {
            resp.sendRedirect(req.getContextPath() + "/jsp/provider.do?method=query");
        }else {
            req.getRequestDispatcher("providermodify.jsp").forward(req, resp);
        }
    }

    private void getProviderById (HttpServletRequest req, HttpServletResponse resp, String url) throws Exception, IOException {
        String id = req.getParameter("proid");
        if (!StringUtils.isNullOrEmpty(id)) {
            ProviderServiceImpl providerService = new ProviderServiceImpl();
            Provider provider = null;
            provider = providerService.getProviderById(id);
            req.setAttribute("provider", provider);
            req.getRequestDispatcher(url).forward(req, resp);
        }
    }

}
