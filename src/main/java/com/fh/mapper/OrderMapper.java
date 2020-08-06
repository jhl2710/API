package com.fh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.model.po.Order;

import java.util.List;

public interface OrderMapper extends BaseMapper<Order> {

    List<Order> queryOrder(Integer id);

}
