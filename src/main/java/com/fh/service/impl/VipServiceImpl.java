package com.fh.service.impl;

import com.fh.mapper.VipMapper;
import com.fh.model.po.Vip;
import com.fh.service.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VipServiceImpl implements VipService {

    @Autowired
    private VipMapper vipMapper;

    @Override
    public Vip queryIponeById(String iphone) {
        return vipMapper.queryIponeById(iphone);
    }
}
