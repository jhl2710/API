package com.fh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.model.po.Detail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DetailMapper extends BaseMapper<Detail> {
    //批量新增
    void batchAdd(@Param("detailList")List<Detail> detailList,@Param("orderId")Integer id);
}
