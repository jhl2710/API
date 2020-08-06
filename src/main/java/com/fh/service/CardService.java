package com.fh.service;

import com.fh.model.vo.CartGoods;

import java.util.List;

public interface CardService {

    Integer addGoodsToCard( Integer id, Integer count);

    List<CartGoods> queryRedisGoods();

    Long deleteCartGoods(Integer id);

    void updateCartGoods(String gids);

    List<CartGoods> queryGoodsCheckCart();

}
