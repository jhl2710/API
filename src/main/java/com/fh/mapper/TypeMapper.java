package com.fh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.model.po.Type;

import java.util.List;

public interface TypeMapper extends BaseMapper<Type> {

    Type queryById(Integer typeId);

}
