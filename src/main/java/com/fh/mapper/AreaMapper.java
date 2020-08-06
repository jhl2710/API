package com.fh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.model.po.Area;
import com.fh.model.po.Vip;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AreaMapper extends BaseMapper<Area> {
    List<String> queryAreaNameByIds(@Param("areaIds")String areaIds);

    Vip queryByName(String vipName);

    void addVip(Vip vip);

}
