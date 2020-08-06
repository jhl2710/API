package com.fh.service.impl;

import com.fh.mapper.AreaMapper;
import com.fh.mapper.GoodsMapper;
import com.fh.model.po.Goods;
import com.fh.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private AreaMapper areaMapper;


    @Override
    public List<Goods> queryAllData(Goods goods) {
        return goodsMapper.queryAllData(goods);
    }

    @Override
    public List<Goods> queryByHot(Goods goods) {
        return goodsMapper.queryByHot(goods);
    }

    @Override
    public List<Goods> queryAllDataById(Goods goods) {

        List<Goods> list = goodsMapper.queryAllDataById(goods);

        for (int i = 0; i < list.size(); i++) {
            String areaId = list.get(i).getAreaId();
            if(StringUtils.isEmpty(areaId)==false){
                List<String> strings = areaMapper.queryAreaNameByIds(areaId);
                StringBuffer ff=new StringBuffer();
                for (int j = 0; j < strings.size(); j++) {
                    ff.append(strings.get(j)).append(",");
                }
                list.get(i).setAreaId(ff.toString().substring(0,ff.lastIndexOf(",")));
            }
        }
        return list;
    }
}
