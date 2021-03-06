package com.fh.service.impl;

import com.fh.mapper.TypeMapper;
import com.fh.model.po.Type;
import com.fh.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeMapper typeMapper;


    @Override
    public List<Type> queryList() {
        return typeMapper.selectList(null);
    }

    @Override
    public Type queryById(Integer typeId) {
        return typeMapper.queryById(typeId);
    }


}
