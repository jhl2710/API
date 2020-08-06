package com.fh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.model.po.Address;
import com.fh.model.vo.AddressInfo;

import java.util.List;

public interface AddressMapper  extends BaseMapper<Address> {
    List<AddressInfo> queryAddress();

    void addAddress(Address address);

}
