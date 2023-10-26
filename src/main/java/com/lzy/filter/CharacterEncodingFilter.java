package com.lzy.filter;


import javax.servlet.*;
import java.io.IOException;

/**
 * @Author 李泽亿
 * @Date: 2023/10/18/ 18:53
 * @description
 */
public class CharacterEncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
