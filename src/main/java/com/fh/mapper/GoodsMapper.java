package com.fh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.model.po.Goods;
import com.fh.model.vo.CartGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper extends BaseMapper<Goods> {
    List<Goods> queryAllData(Goods goods);

    List<Goods> queryByHot(Goods goods);

    List<Goods> queryAllDataById(Goods goods);

    CartGoods queryGoodsById(Integer id);

    Goods queryById(Integer id);

    int updateGoods(@Param("id")Integer id,@Param("count")Integer count);

}
