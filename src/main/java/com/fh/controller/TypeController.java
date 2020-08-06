package com.fh.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.JsonData;
import com.fh.model.po.Type;
import com.fh.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("TypeController")
public class TypeController {

    @Autowired
    private TypeService typeService;


    //第一种（原始的）
    @RequestMapping("queryList")
    @ResponseBody
    //@CrossOrigin
    public Map queryList(){
        Map map=new HashMap();
        try {
           List<Type> list= typeService.queryList();
           map.put("data",list);
           map.put("code",200);
           map.put("message","success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    //第二种方法
    @RequestMapping("queryList2")
    @ResponseBody
    public JsonData queryList2(){
        try {
            List<Type> list= typeService.queryList();
            return  JsonData.getJsonSuccess(list);
        } catch (Exception e) {
            return  JsonData.getJsonError(e.getMessage());
        }
    }

    //第三种方法
    @RequestMapping("queryList3")
    @ResponseBody
    public JsonData queryList3(){
        //全局异常
        //List<Type> list= typeService.queryList();
         Jedis jedis=new Jedis("192.168.178.129");
         return  JsonData.getJsonSuccess(jedis.get("typeJsonData"));
    }


    //刷新缓存
    @RequestMapping("refreshQueryType")
    @ResponseBody
    public JsonData refreshQueryType(){
        List<Type> list= typeService.queryList();
        String s = JSONObject.toJSONString(list);
        Jedis jedis=new Jedis("192.168.178.129");
        jedis.set("typeJsonData",s);
        jedis.close();
        return JsonData.getJsonSuccess("刷新成功");
    }



}
