package com.fh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.mapper.AddressMapper;
import com.fh.model.po.Address;
import com.fh.model.po.Vip;
import com.fh.model.vo.AddressInfo;
import com.fh.service.AddressService;
import com.fh.util.RedisUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private HttpServletRequest request;

    @Override
    public List<AddressInfo> queryAddress() {
        Vip user = (Vip) request.getAttribute("user");
        String iphone = (String) user.getNum();

        //构建条件
        QueryWrapper qw=new QueryWrapper();
        qw.eq("vipId",iphone);
        List<Address> list = addressMapper.selectList(qw);

        //实际要的数据
        List<AddressInfo> addressInfo=new ArrayList<AddressInfo>();
        for (int i = 0; i < list.size(); i++) {
            AddressInfo temp=new AddressInfo();
            Address address = list.get(i);
            temp.setId(address.getId());
            temp.setName(address.getName());
            temp.setIscheck(address.getIsCheck());
            temp.setiPhone(address.getIphone());
            String areaName= RedisUse.getAreaName(address.getAreaId());
            String detailArea = address.getDetailArea();
            temp.setAddress(areaName+detailArea);

            addressInfo.add(temp);
        }
        return addressInfo;
    }



    @Override
    public void addAddress(Address address) {
        addressMapper.addAddress(address);
    }


}
