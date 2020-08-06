package com.fh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.CountException;
import com.fh.common.enums.PayStatusEnum;
import com.fh.mapper.DetailMapper;
import com.fh.mapper.GoodsMapper;
import com.fh.mapper.OrderMapper;
import com.fh.model.po.Detail;
import com.fh.model.po.Goods;
import com.fh.model.po.Order;
import com.fh.model.po.Vip;
import com.fh.model.vo.CartGoods;
import com.fh.service.OrderService;
import com.fh.util.RedisUse;
import com.github.wxpay.sdk.FeiConfig;
import com.github.wxpay.sdk.WXPay;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private DetailMapper detailMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private GoodsMapper goodsMapper;


    @Override
    public Map addOrder(Integer addressId, Integer payType) throws CountException {
        //构建此次请求返回的数据
        Map map=new HashMap();
        //订单详情表
        List<Detail> detailList=new ArrayList<>();
        //完善数据 保存到数据库
        Order order=new Order();
        order.setCreateDate(new Date());
        order.setAddressId(addressId);
        order.setPayType(payType);

        order.setPayStatus(PayStatusEnum.PAY_STATUS_INIT.getStatus());//枚举

        //设计商品的清单个数  前提是数据库里的库存要够
        Integer typeCount=0;
        //设计总金额
        BigDecimal totalMoney=new BigDecimal(0);//默认值 0
        //从redis 取出购物车数据  返回
        Vip user = (Vip) request.getAttribute("user");
        String iphone = (String) user.getNum();

        order.setVipId(user.getId());

        List<String> goodsStr = RedisUse.hvals("cart_" + iphone + "_jhl");
        for (int i = 0; i < goodsStr.size(); i++) {
            CartGoods cartGoods = JSONObject.parseObject(goodsStr.get(i), CartGoods.class);
            //判断是否为选中的商品
            if(cartGoods.getCheck()==true){
                //查询数据库的数据 判断库存是否够
                Goods goods = goodsMapper.queryById(cartGoods.getId());
                //判断库存是否够
                if(goods.getKucun()>=cartGoods.getCount()){//数据库的库存大于购物车里的库存
                    //第一  商品的清单个数加一  计算总金额
                    typeCount++;
                    totalMoney=totalMoney.add(cartGoods.getMoney());
                    //维护订单详情表
                    Detail detail=new Detail();
                    detail.setCount(cartGoods.getCount());
                    detail.setGoodsId(cartGoods.getId());
                    detailList.add(detail);//注意id
                    //第二 减掉库存
                    //解决超卖问题
                    int i1 = goodsMapper.updateGoods(goods.getId(), cartGoods.getCount());
                    if(i1==0){
                        throw new  CountException("商品编号为:"+goods.getName()+"的库存不足 库存只有："+goods.getKucun());
                    }
                }else {
                    //库存不够
                    throw new  CountException("商品编号为:"+goods.getName()+"的库存不足 库存只有："+goods.getKucun());
                }
            }
        }

        order.setTotalMoney(totalMoney);
        order.setTypeCount(typeCount);
        orderMapper.insert(order);

        //保存订单详情信息
        detailMapper.batchAdd(detailList,order.getId());

        //删除购物车里面已经结算过的商品  从redis里面移除
        for (int i = 0; i < goodsStr.size(); i++) {
            CartGoods cartGoods = JSONObject.parseObject(goodsStr.get(i), CartGoods.class);
            //判断是否为选中的商品
            if(cartGoods.getCheck()==true){
                RedisUse.hdel("cart_" + iphone + "_jhl",cartGoods.getId().toString());
            }
        }

        map.put("orderId",order.getId());
        map.put("totalMoney",totalMoney);

        return map;
    }

    @Override
    public Map weChatPay(Integer orderId) throws Exception {
        Map rs=new HashMap();
        //从redsi中判断是否已经生成过
        String url = RedisUse.get("order_"+orderId+"_jhl");
        if(StringUtils.isEmpty(url)!=true){//不为空  已经生成过二维码
            rs.put("code",200);
            rs.put("url",url);
            return rs;
        }

        Order order = orderMapper.selectById(orderId);
        // 微信支付  natvie   商户生成二维码
        //配置配置信息
        FeiConfig config = new FeiConfig();
        //得到微信支付对象
        WXPay wxpay = new WXPay(config);
        //设置请求参数
        Map<String, String> data = new HashMap<String, String>();
        //对订单信息描述
        data.put("body", "飞狐电商-订单支付");
        //String payId = System.currentTimeMillis()+"";
        //设置订单号 （保证唯一 ）
        data.put("out_trade_no","weixin1_order_jhl"+orderId);
        //设置币种
        data.put("fee_type", "CNY");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        Date d=new Date();
        String dateStr = sdf.format(new Date(d.getTime() + 120000000));
        //设置二维码的失效时间
        data.put("time_expire", dateStr);
        //设置订单金额   单位分
        data.put("total_fee","1");
        data.put("notify_url", "http://www.example.com/wxpay/notify");
        //设置支付方式
        data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
        // 统一下单
        Map<String, String> resp = wxpay.unifiedOrder(data);
        //这一块必须用log4j 做记录的
        System.out.println(orderId+"下订单结果为:"+ JSONObject.toJSONString(resp));
        if("SUCCESS".equalsIgnoreCase(resp.get("return_code"))&&"SUCCESS".equalsIgnoreCase(resp.get("result_code"))){
            rs.put("code",200);
            rs.put("url",resp.get("code_url"));
            //更新订单状态
            order.setPayStatus(PayStatusEnum.PAY_STATUS_ING.getStatus());
            orderMapper.updateById(order);

            //将二维码存入redis  设置失效时间
            RedisUse.set("order_"+orderId+"_jhl",resp.get("code_url"),30*60);
        }else {
            rs.put("code",600);
            rs.put("info",resp.get("return_msg"));
        }
        return rs;
    }

    @Override
    public Integer queryPayStatus(Integer orderId) throws Exception {
        FeiConfig config = new FeiConfig();
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no","weixin1_order_jhl"+orderId);
        // 查询支付状态
        Map<String, String> resp = wxpay.orderQuery(data);

        System.out.println("查询结果："+JSONObject.toJSONString(resp));


        if("SUCCESS".equalsIgnoreCase(resp.get("return_code"))&&"SUCCESS".equalsIgnoreCase(resp.get("result_code"))){
            if("SUCCESS".equalsIgnoreCase(resp.get("trade_state"))){//支付成功
                //更新
                Order order=new Order();
                order.setId(orderId);
                order.setPayStatus(PayStatusEnum.PAY_STATUS_SUCCESS.getStatus());
                orderMapper.updateById(order);
                return 1;
            }else if("USERPAYING".equalsIgnoreCase(resp.get("trade_state"))){
                return 2;
            }else if("NOTPAY".equalsIgnoreCase(resp.get("trade_state"))){
                return 3;
            }
        }
        return 0;
    }

    @Override
    public List<Order> queryOrder() {
        Vip user = (Vip) request.getAttribute("user");
        return orderMapper.queryOrder(user.getId());
    }


}
