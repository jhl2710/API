package com.fh.controller;

import com.fh.common.JsonData;
import com.fh.model.vo.CartGoods;
import com.fh.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("CartController")
public class CartController {

    @Autowired
    private CardService cardService;


    //加入购物车
    @RequestMapping("addCart")
    @ResponseBody
    public JsonData addCart(Integer id,Integer count){
        Integer cardCount=cardService.addGoodsToCard(id,count);
        return JsonData.getJsonSuccess(cardCount);
    }

    //查询     从redis里面查询购物车数据
    @RequestMapping("queryRedisGoods")
    @ResponseBody
    public JsonData queryRedisGoods(){
        List<CartGoods> cartGoodsList =cardService.queryRedisGoods();
        return JsonData.getJsonSuccess(cartGoodsList);

    }

    //删除
    @RequestMapping("deleteCartGoods")
    @ResponseBody
    public JsonData deleteCartGoods(Integer id){
        Long count=cardService.deleteCartGoods(id);
        return JsonData.getJsonSuccess(count);
    }

    //修改
    @RequestMapping("updateCartGoods")
    @ResponseBody
    public JsonData updateCartGoods(String gids){
        cardService.updateCartGoods(gids);
        return JsonData.getJsonSuccess("修改成功");
    }


    //查询购物车中要结算的商品信息
    @RequestMapping("queryGoodsCheckCart")
    @ResponseBody
    public JsonData queryGoodsCheckCart(){
        List<CartGoods> list=cardService.queryGoodsCheckCart();
        return JsonData.getJsonSuccess(list);
    }





}
