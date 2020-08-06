package com.fh.service;

import com.fh.model.po.Area;
import com.fh.model.po.Vip;

import java.util.List;

public interface AreaService {
    List<Area> queryList();

    Vip queryByName(String vipName);

    void addVip(Vip vip);

    
}
