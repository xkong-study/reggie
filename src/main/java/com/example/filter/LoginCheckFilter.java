package com.example.filter;

import com.alibaba.fastjson.JSON;
import com.example.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器
    public static final AntPathMatcher pathMatcher = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1、获取本次清求的URL
        String requestURI = request.getRequestURI();
        //2、判断本次请求是否需要处理
        String[] urlQueue = new String[]{

        };
        boolean needProcess = check(urlQueue, requestURI);
        log.info("Checking if need to process: {}, Result: {}", requestURI, needProcess);
        //3、如果不需要处理，则直接放行
        if(!needProcess){
            log.info("ok"+ requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //4、判渐登灵状态，如果已登录，则直接成行
        //5、如果没登灵则返回夫登录结果
        Object user = request.getSession().getAttribute("employee");
        if(user!=null){
            Long id = Thread.currentThread().getId();
            log.info("线程id为:{}",id);
            filterChain.doFilter(request,response);
        }

        Object employee = request.getSession().getAttribute("user");
        if(employee!=null){
            Long id = Thread.currentThread().getId();
            log.info("线程id为:{}",id);
            filterChain.doFilter(request,response);
        }
        // 5.如果未登录，则返回未登录结果
        // 由于前端代码中引入了js/request.js，相应拦截器会帮我们跳转到登录页面，所以这里不需要跳转，只需要返回未登录结果即可
        // 即只要通过输出流,包装通用返回结果类，返回未登录结果即可
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        log.info("拦截到请求:{}",request.getRequestURI());
        return;
    }
    public boolean check(String[] urlQueue,String requestURI){
        for(String u:urlQueue){
            boolean match =pathMatcher.match(requestURI,u);
            if(match){return true;};
        };
        return false;
    };
}
