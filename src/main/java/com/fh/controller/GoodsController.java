package com.fh.controller;

import com.fh.common.JsonData;
import com.fh.model.po.Goods;
import com.fh.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("GoodsController")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("queryAllData")
    @ResponseBody
    public JsonData queryAllData(Goods goods){
        List<Goods> goodsList = goodsService.queryAllData(goods);
        return JsonData.getJsonSuccess(goodsList);
    }



    @RequestMapping("queryByHot")
    @ResponseBody
    public JsonData queryByHot(Goods goods){
        List<Goods> list = goodsService.queryByHot(goods);
        return JsonData.getJsonSuccess(list);
    }


    //
    @RequestMapping("queryAllDataById")
    @ResponseBody
    public JsonData queryAllDataById(Goods goods){
        List<Goods> goodsList = goodsService.queryAllDataById(goods);
        return JsonData.getJsonSuccess(goodsList);
    }



}
