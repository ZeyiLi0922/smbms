package com.lzy.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.lzy.pojo.Role;
import com.lzy.pojo.User;
import com.lzy.service.role.RoleService;
import com.lzy.service.role.RoleServiceImpl;
import com.lzy.service.user.UserService;
import com.lzy.service.user.UserServiceImpl;
import com.lzy.util.Constants;
import com.lzy.util.PageSupport;
import com.mysql.cj.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author 李泽亿
 * @Date: 2023/10/23/ 21:50
 * @description
 */
@SuppressWarnings("all")
//实现Servlet复用
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method.equals("savepwd") && method != null) {
            this.updatePwd(req, resp);
        } else if (method.equals("pwdmodify") && method != null) {
            this.pwdModify(req, resp);
        } else if (method.equals("query") && method != null) {
            try {
                this.query(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (method.equals("add") && method != null) {
            try {
                this.add(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else if (method.equals("getrolelist") && method != null) {
            try {
                this.getRoleList(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (method.equals("ucexist")) {
            try {
                this.userCodeExist(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (method.equals("deluser") && method != null) {
            try {
                this.delUser(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (method.equals("view") && method != null) {
            try {
                this.getUserById(req, resp, "userview.jsp");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (method.equals("modify") && method != null) {
            try {
                this.getUserById(req, resp, "usermodify.jsp");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (method.equals("modifyexe") && method != null) {
            try {
                this.modify(req, resp);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void query(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException, ServletException {
         //查询用户列表
        String queryUserName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;

        //获取用户列表
        UserService userService = new UserServiceImpl();

        //第一次走页面一定是第一页，页面大小固定
        //设置页面容量
        int pageSize = Constants.pageSize;
        //当前页码
        int currentPageNo = 1;

        System.out.println("queryUserName servlet --------> " + queryUserName);
        System.out.println("queryUserRole servlet --------> " + temp);
        System.out.println("pageIndex servlet ---------> " + pageIndex);

        if (queryUserName == null) {
            queryUserName = "";
        }
        if (temp != null && !temp.equals("")) {
            queryUserRole = Integer.parseInt(temp); //给查询赋值，默认是0
        }
        if (pageIndex != null) {
            try {
                currentPageNo = Integer.valueOf(pageIndex);
            } catch (NumberFormatException e) {
                resp.sendRedirect("error.jsp");
            }
        }

        //获取用户总数量（分页：存在上一页 下一页的情况）
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
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

        //获取用户列表展示
        List<User> userList = null;
        userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList", userList);

        List<Role> roleList = null;
        RoleService roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();
        req.setAttribute("roleList", roleList);

        req.setAttribute("queryUserName", queryUserName);
        req.setAttribute("queryUserRole", queryUserRole);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currentPageNo);

        //返回前端
        req.getRequestDispatcher("userlist.jsp").forward(req, resp);
    }
    //修改密码
    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从Session中拿ID
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");
        boolean flag = false;
        if (o != null && !StringUtils.isNullOrEmpty(newpassword)) {
            UserService userService = new UserServiceImpl();
            try {
                flag = userService.updatePwd(((User) o).getId(), newpassword);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (flag) {
                req.setAttribute("message", "修改密码成功，请退出，使用新密码登陆");
                //密码修改成功，移除当前Session
                req.getSession().removeAttribute(Constants.USER_SESSION);
            } else {
                req.setAttribute("message", "密码修改失败");
            }
        } else {
            req.setAttribute("message", "新密码有问题");
            //密码修改成功，移除当前Session
        }

        req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
    }

    //验证旧密码，Session中有用户的密码
    public void pwdModify(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //从Session里面拿ID
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");

        //万能的Map : 结果集
        Map<String, String> resultMap = new HashMap<String, String>();
        if (o == null) //Session失效了，或者session过期了
        {
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldpassword)) { //输入的密码为空
            resultMap.put("result", "error");
        } else {
            String userPassword = ((User) o).getUserPassword(); //Session中用户的密码
            if (oldpassword.equals(userPassword)) {
                resultMap.put("result", "true");
            } else {
                resultMap.put("result", "false");
            }
        }

        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException, ParseException {
        System.out.println("add()==============");
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User) req.getSession().getAttribute(Constants.USER_SESSION)).getId());

        UserService userService = new UserServiceImpl();
        if (userService.add(user)) {
            resp.sendRedirect(req.getContextPath() + "/jsp/user.do?method=query");
        } else {
            req.getRequestDispatcher("useradd.jsp").forward(req, resp);
        }
    }

    public void delUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        String id = req.getParameter("uid");
        Integer delId = 0;
        try {
            delId = Integer.parseInt(id);
        } catch (Exception e) {
            delId = 0;
        }
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (delId <= 0) {
            resultMap.put("delRusult", "notexist");
        } else {
            UserService userService = new UserServiceImpl();
            if (userService.deleteUserById(delId)) {
                resultMap.put("delResult", "true");
            } else {
                resultMap.put("delReuslt", "false");
            }
        }

        //吧resultMap转换成json对象输出
        resp.setContentType("application/json");
        PrintWriter outPrintWriter = resp.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    private void modify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, ParseException {
        String id = req.getParameter("uid");
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user = new User();
        user.setId(Integer.valueOf(id));
        user.setUserName(userName);
        user.setGender(Integer.valueOf(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        user.setModifyBy(((User) req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());

        UserServiceImpl userService = new UserServiceImpl();
        if (userService.modify(user)) {
            resp.sendRedirect(req.getContextPath() + "/jsp/user.do?method=query");
        } else {
            req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
        }
    }

    private void getUserById(HttpServletRequest req, HttpServletResponse resp, String url) throws Exception {
        String id = req.getParameter("uid");
        if (!StringUtils.isNullOrEmpty(id)) {
            //调用后台方法得到user对象
            UserServiceImpl userService = new UserServiceImpl();
            User user = userService.getUserById(id);
            req.setAttribute("user", user);
            req.getRequestDispatcher(url).forward(req, resp);
        }
    }

    private void userCodeExist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        //判断用户账号是否可用
        String userCode = req.getParameter("userCode");

        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (StringUtils.isNullOrEmpty(userCode)) {
            //userCode == null || userCode.equals("")
            resultMap.put("userCode", "null");
        } else {
            UserServiceImpl userService = new UserServiceImpl();
            User user = userService.selectUserCodeExist(userCode);
            if (user != null) {
                resultMap.put("userCode", "exist");
            } else {
                resultMap.put("userCode", "notexist");
            }
        }

        //把resultMap转为json字符串以json的形式输出
        //配置上下文的输出形式
        resp.setContentType("application/json");
        //从resp对象中获取往外输出的writer对象
        PrintWriter outprintWriter = resp.getWriter();
        //把resultMap转为json字符串 输出
        outprintWriter.write(JSONArray.toJSONString(resultMap));
        outprintWriter.flush(); //刷新
        outprintWriter.close(); //关闭流
    }

    private void getRoleList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        List<Role> roleList = null;
        RoleService roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();
        //把roleList转换成json对象输出
        resp.setContentType("application/json");
        PrintWriter outPrintWriter = resp.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(roleList));
        outPrintWriter.flush();
        outPrintWriter.close();
    }
}
