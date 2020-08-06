package com.fh.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CrossInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求的域名
        String yuming = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", yuming);
        //允许session //是否支持cookie跨域
        response.setHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers","x-requested-with");
        response.setHeader("Access-Control-Allow-Credentials","true");

        //options请求特殊处理
        //当客户端修改了头信息  会发起两个请求  第一个是预请求options （是否允许修改头信息）   另一个是发起真正的请求
        String method = request.getMethod();
        if(method.equalsIgnoreCase("options")){
            //允许修改头信息  添加一个name属性
            response.setHeader("Access-Control-Allow-Headers","token");
            return false;
        }


        //从header 里去数据 方便以后的需求使用到
        String name = request.getHeader("login_token");
        return true;
    }
}
