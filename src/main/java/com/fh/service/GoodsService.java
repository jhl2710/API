package com.fh.service;

import com.fh.model.po.Goods;

import java.util.List;

public interface GoodsService {
    List<Goods> queryAllData(Goods goods);

    List<Goods> queryByHot(Goods goods);

    List<Goods> queryAllDataById(Goods goods);

}
