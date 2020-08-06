package com.fh.util;

import org.junit.Test;
import java.io.File;



public class TestOss {

    @Test
    public void test(){
        File img=new File("D:\\飞狐教育\\pictures\\2435.jpg");
        String s = OSSUtils.uploadFile(img);
        System.out.println(s);
    }
}
