package com.fh.interceptor;

import com.fh.common.NologinException;
import com.fh.model.po.Vip;
import com.fh.util.JWT;
import com.fh.util.RedisUse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

//登录的拦截器
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        Object login_user = request.getSession().getAttribute("login_user");
//        String token = request.getParameter("token");
        String token = request.getHeader("token");
        //判断请求头中的信息是否完整
        if(StringUtils.isEmpty(token)){
            throw new Exception("没有登录");
        }

        //验签
        //解密  得到字节数组
        byte[] decode = Base64.getDecoder().decode(token);
        //将 字节数组 转为 string 格式：iphone+","+sign
        String  signToken=new String(decode);
        String[] split = signToken.split(",");
        //判断信息是否被篡改
        if(split.length!=2){//被篡改
            throw new Exception("信息格式错误");
        }

        String iphone=split[0];//手机号码
        String sign=split[1];//jwt解密出的密钥


        Vip user = JWT.unsign(sign, Vip.class);
        if(user==null){
            System.out.println("没有登录");
            //返回json字符串
            throw new NologinException("没有登录");
         /*   response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSONObject.toJSONString(JsonData.getJsonError(1000,"没有登录")));*/

        }
        if(user!=null){
            //获取redis中的密钥进行判断是否为最新的
            String sign_redis = RedisUse.get("token_" + iphone);
            if(!sign.equals(sign_redis)){
                throw new NologinException("验证过期 重新登录");
            }
        }
        //续命
        RedisUse.set("token_"+iphone,sign,60*30);
        //将用户信息放入request中  方便后面需求处理
        request.setAttribute("user",user);
        return true;
    }
}
