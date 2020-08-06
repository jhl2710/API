package com.fh.service;

import com.fh.model.po.Type;

import java.util.List;

public interface TypeService {

    List<Type> queryList();

    Type queryById(Integer typeId);

}
