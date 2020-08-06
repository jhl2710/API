package com.fh.service;

import com.fh.common.CountException;
import com.fh.model.po.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Map addOrder(Integer addressId, Integer payType)throws CountException;

    Map weChatPay(Integer orderId) throws Exception;

    Integer queryPayStatus(Integer orderId) throws Exception;

    List<Order> queryOrder();

}
