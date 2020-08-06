package com.fh.controller;

import com.fh.common.JsonData;
import com.fh.model.po.Vip;
import com.fh.service.VipService;
import com.fh.util.JWT;
import com.fh.util.RedisUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("vip/")
public class VipController {

    @Autowired
    private VipService vipService;

    @RequestMapping("sendMessage")
    public JsonData sendMessage(String iphone){
        //发送短信   阿里提供的短信服务
        //String code = MessageUtils.sendMsg(iphone);
        String code="1111";
        //存redis
        RedisUse.set(iphone+"_jhl",code,60*5);
        return JsonData.getJsonSuccess("短信发送成功");
    }

    //登录
    @RequestMapping("login")
    public JsonData login(String iphone, String code, HttpServletRequest request){
        Map logMap=new HashMap();
        //正确将用户信息存入session中f
        Vip v = vipService.queryIponeById(iphone);
        //获取code
        String redis_code = RedisUse.get(iphone + "_jhl");
        if(redis_code!=null&&redis_code.equals(code)){
            // 生成一个秘钥   对应一个信息
            Vip user=new Vip();
            user.setNum(iphone);
            user.setId(v.getId());
            String sign = JWT.sign(user,1000 * 60 * 60 * 24);

            //加签
            String token = Base64.getEncoder().encodeToString((iphone+","+sign).getBytes());

            // 将最新的秘钥保存到redis中  生成多个秘钥  最新的是有用的
            RedisUse.set("token_"+iphone,sign,60*30);

            logMap.put("status","200");
            logMap.put("message","登录成功");
            logMap.put("token",token);
        }else{
            logMap.put("status","300");
            logMap.put("message","用户不存在 或者 验证码错误");
        }
        return JsonData.getJsonSuccess(logMap);
    }













}
