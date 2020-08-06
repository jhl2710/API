package com.fh.controller;

import com.fh.common.JsonData;
import com.fh.model.po.Area;
import com.fh.model.po.Vip;
import com.fh.service.AreaService;
import com.fh.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("AreaController")
public class AreaController {

    @Autowired
    private AreaService areaService;

    //第一种方法（原始的）
    @RequestMapping("queryList")
    @ResponseBody
    public Map queryList(){
        Map map=new HashMap();
        try {
            List<Area> list=areaService.queryList();
            map.put("data",list);
            map.put("code",200);
            map.put("message","success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    //第二种方法
    @RequestMapping("queryList2")
    @ResponseBody
    public JsonData queryList2(){
        try {
            List<Area> list=areaService.queryList();
            return JsonData.getJsonSuccess(list);
        } catch (Exception e) {
            return JsonData.getJsonError(e.getMessage());
        }
    }


    //第三种方法（全局异常）
    @RequestMapping("queryList3")
    @ResponseBody
    public JsonData queryList3(){
        List<Area> list=areaService.queryList();
        return JsonData.getJsonSuccess(list);
    }

    //验证用户名是否存在(注册验证)
    @RequestMapping("checkName")   //{valid:false}
    @ResponseBody
    public Map checkName(String vipName){
        Map map=new HashMap();
        Vip vip=areaService.queryByName(vipName);
        if(vip==null){
            map.put("valid",true);
        }else{
            map.put("valid",false);
        }
        return map;
    }

    //vip注册
    @RequestMapping("addVip")
    @ResponseBody
    public Map addVip(Vip vip){
        Map map=new HashMap();
        try {
            vip.setBrithday(new Date());
            areaService.addVip(vip);
            map.put("code",200);
        } catch (Exception e) {
            map.put("message",e.getMessage());
            map.put("code",500);
        }
        return map;
    }


    //图片上传
    //文件上传
    @RequestMapping("OSSFileUpload")
    @ResponseBody
    public Map OSSFileUpload( @RequestParam("picture") MultipartFile picture) {
        Map map = new HashMap();
        try {
            File file = FileUtil.readFiles(picture);
            String filePath = FileUtil.uploadFile(file);
            map.put("filePath",filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
