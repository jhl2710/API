package com.fh.controller;

import com.fh.common.JsonData;
import com.fh.model.po.Address;
import com.fh.model.vo.AddressInfo;
import com.fh.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("AddressController")
public class AddressController {

    @Autowired
    private AddressService addressService;


    //查询
    @RequestMapping("queryAddress")
    @ResponseBody
    private JsonData queryAddress(){
        List<AddressInfo> list=addressService.queryAddress();
        return JsonData.getJsonSuccess(list);
    }


    //新增
    @RequestMapping("addAddress")
    @ResponseBody
    public JsonData addAddress(Address address){
        addressService.addAddress(address);
        return JsonData.getJsonSuccess("添加成功");
    }


}
