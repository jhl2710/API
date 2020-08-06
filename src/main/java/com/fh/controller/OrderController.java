package com.fh.controller;

import com.fh.common.CountException;
import com.fh.common.JsonData;
import com.fh.model.po.Order;
import com.fh.service.OrderService;
import com.fh.util.RedisUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("OrderController")
public class OrderController {

    @Autowired
    private OrderService orderService;


    //生成订单
    @RequestMapping("addOrder")
    @ResponseBody
    private JsonData addOrder(Integer addressId,Integer payType,String flag) throws CountException {
        //处理幂等性问题  同一个请求 同发送 结果只能是一个
        boolean exists = RedisUse.exists(flag);
        if(exists==true){
            return JsonData.getJsonError(300,"请求处理中");
        }else {
            RedisUse.set(flag,"",10);
        }
        Map map=orderService.addOrder(addressId,payType);
        return JsonData.getJsonSuccess(map);
    }


    //统一下单   返回二维码url
    @RequestMapping("weChatPay")
    @ResponseBody
    public JsonData weChatPay(Integer orderId) throws Exception {
        Map map=orderService.weChatPay(orderId);
        return JsonData.getJsonSuccess(map);
    }


    //查询订单状态
    @RequestMapping("queryPayStatus")
    @ResponseBody
    public JsonData queryPayStatus(Integer orderId) throws Exception {
        Integer status=orderService.queryPayStatus(orderId);
        return JsonData.getJsonSuccess(status);
    }

    //查询订单
    @RequestMapping("queryOrder")
    @ResponseBody
    public JsonData queryOrder(){
        List<Order> list=orderService.queryOrder();
        return JsonData.getJsonSuccess(list);
    }

}
