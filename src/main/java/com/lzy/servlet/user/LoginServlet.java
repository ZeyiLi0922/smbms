package com.lzy.servlet.user;

import com.lzy.pojo.User;
import com.lzy.service.user.UserServiceImpl;
import com.lzy.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @Author 李泽亿
 * @Date: 2023/10/18/ 21:47
 * @description
 */
public class LoginServlet extends HttpServlet{
    //Servlet:控制层，调用业务层代码
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LoginServlet--start");
        //获取用户名和密码
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");

        //和数据库中的密码进行对比，调用业务层
        UserServiceImpl userService = new UserServiceImpl();
        try {
            User user = userService.login(userCode, userPassword);
            String userPassword1 = user.getUserPassword();
            if (user != null && Objects.equals(userPassword1, userPassword)) { //查无此人，可以登陆
                req.getSession().setAttribute(Constants.USER_SESSION, user);
                    //跳转到内部主页
                resp.sendRedirect("jsp/frame.jsp");
            } else { //查无此人，无法登陆
                //转发回登陆页面，顺带提示它，用户名或密码错误
                req.setAttribute("error", "用户名或者密码不正确");
                req.getRequestDispatcher("login.jsp").forward(req, resp);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
