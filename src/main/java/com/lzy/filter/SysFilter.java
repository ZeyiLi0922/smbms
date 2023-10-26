package com.lzy.filter;

import com.lzy.pojo.User;
import com.lzy.util.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.lzy.util.Constants.USER_SESSION;

/**
 * @Author 李泽亿
 * @Date: 2023/10/23/ 20:17
 * @description
 */
public class SysFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        //过滤器，从Session中获取用户
        User user = (User)request.getSession().getAttribute(Constants.USER_SESSION);

        if (user == null) {
            response.sendRedirect("/smbms/error.jsp");
        } else {
            chain.doFilter(req, resp);
        }

    }

    @Override
    public void destroy() {

    }
}
