package com.fh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.mapper.GoodsMapper;
import com.fh.model.po.Goods;
import com.fh.model.po.Vip;
import com.fh.model.vo.CartGoods;
import com.fh.service.CardService;
import com.fh.util.RedisPoolUtils;
import com.fh.util.RedisUse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private HttpServletRequest request;


    //加入购物车  新增
    @Override
    public Integer addGoodsToCard( Integer id, Integer count) {

        // 将数据存入redis   hash  key 用户的唯一标示  filed 商品id  value  商品信息
        Vip user = (Vip) request.getAttribute("user");
        String iphone = (String) user.getNum();

        //判断库存是否够
        if(count>0){
            Goods goods = goodsMapper.queryById(id);
            if(count>goods.getKucun()){
                return goods.getKucun()-count;
            }
        }

        //获取用户的购物车
        String hget = RedisUse.hget("cart_" + iphone + "_jhl", id.toString());
        //判断是否存在
        if(StringUtils.isEmpty(hget)){//不存在 进行新增
            CartGoods cartGoods=goodsMapper.queryGoodsById(id);
            cartGoods.setCount(count);
            cartGoods.setCheck(true);
            //计算小计
            BigDecimal money=cartGoods.getPrice().multiply(new BigDecimal(count));
            cartGoods.setMoney(money);
            //将商品转为字符串
            String s = JSONObject.toJSONString(cartGoods);
            //存入redis
            RedisUse.hset("cart_"+iphone+"_jhl",id.toString(),s);

        }else {//存在  进行修改
            CartGoods cartGoods =JSONObject.parseObject(hget,CartGoods.class);

            cartGoods.setCount(cartGoods.getCount()+count);

            if(cartGoods.getCount()<=1){
                cartGoods.setCount(1);
            }

            //判断库存是否够   情景：购物车里点击+号
            Goods goods = goodsMapper.queryById(id);
            if(cartGoods.getCount()>goods.getKucun()){
                return goods.getKucun()-cartGoods.getCount();
            }


            BigDecimal money=cartGoods.getPrice().multiply(new BigDecimal(cartGoods.getCount()));
            cartGoods.setMoney(money);
            //将商品转为字符串
            String s = JSONObject.toJSONString(cartGoods);
            //存入redis
            RedisUse.hset("cart_"+iphone+"_jhl",id.toString(),s);
        }

        //获取商品种类的个数
        long hlen = RedisUse.hlen("cart_"+iphone+"_jhl");
        return (int) hlen;
    }

    //查询
    @Override
    public List<CartGoods> queryRedisGoods() {
        Vip user = (Vip) request.getAttribute("user");
        String iphone = (String) user.getNum();
        Map<String, String> stringStringMap= RedisUse.hgetAll("cart_" + iphone + "_jhl");

        List<CartGoods> cartGoodsList = new ArrayList<CartGoods>();
        Iterator<Map.Entry<String, String>> iterator = stringStringMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            String value = next.getValue();
            CartGoods cartGoods = JSONObject.parseObject(value, CartGoods.class);
            cartGoodsList.add(cartGoods);
        }
        return cartGoodsList;
    }



    //删除
    @Override
    public Long deleteCartGoods(Integer id) {
        Vip user = (Vip) request.getAttribute("user");
        String iphone = (String) user.getNum();

        Jedis jedis = RedisPoolUtils.getJedis();

        jedis.hdel("cart_"+iphone+"_jhl",id.toString());

        long hlen = RedisUse.hlen("cart_" + iphone + "_jhl");
        return hlen;
    }



    //修改
    @Override
    public void updateCartGoods(String gids) {
        Vip user = (Vip) request.getAttribute("user");
        String iphone = (String) user.getNum();

        List<String> list = RedisUse.hvals("cart_" + iphone + "_jhl");
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            CartGoods cartGoods = JSONObject.parseObject(s, CartGoods.class);
            Integer id = cartGoods.getId();
            if((","+gids).contains(","+id+",")==true){
                cartGoods.setCheck(true);
                RedisUse.hset("cart_" + iphone + "_jhl",cartGoods.getId().toString(),JSONObject.toJSONString(cartGoods));
            }else {
                cartGoods.setCheck(false);
                RedisUse.hset("cart_" + iphone + "_jhl",cartGoods.getId().toString(),JSONObject.toJSONString(cartGoods));
            }
        }
    }

    @Override
    public List<CartGoods> queryGoodsCheckCart() {
        //从redis 取出购物车数据  返回
        Vip user = (Vip) request.getAttribute("user");
        String iphone = (String) user.getNum();

        List<String> list = RedisUse.hvals("cart_" + iphone + "_jhl");

        //实际需要的数据
        List<CartGoods> cartGoodsList=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            CartGoods cartGoods= JSONObject.parseObject(s, CartGoods.class);
            if(cartGoods.getCheck()==true){
                cartGoodsList.add(cartGoods);
            }
        }
        return cartGoodsList;
    }


}
