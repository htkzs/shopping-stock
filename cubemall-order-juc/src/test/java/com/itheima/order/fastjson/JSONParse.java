package com.itheima.order.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itheima.order.entity.CartInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JSONParse {
    CartInfo cartInfo =new CartInfo(1001,"OK");
    @Test
    public void ObjectToJsonString(){
        String jsonString = JSON.toJSONString(cartInfo);
        CartInfo cartInfo = JSON.parseObject(jsonString, CartInfo.class);
        //CartInfo(cattId=1001, statusCode=OK)
        System.out.println(cartInfo);
        //{"cattId":1001,"statusCode":"OK"}
        System.out.println(jsonString);
        CartInfo cartInfo1 = JSONObject.parseObject(jsonString, CartInfo.class);
        //CartInfo(cattId=1001, statusCode=OK)
        System.out.println(cartInfo1);
        //将java对象转会为JSON字符串
        String toJSONString = JSONObject.toJSONString(cartInfo1);
        //将JSON字符串转换为JSONObject
        JSONObject jsonObject = JSONObject.parseObject(toJSONString);
        //{"cattId":1001,"statusCode":"OK"}
        System.out.println(jsonObject);
        //将JSON字符串转换为JAVA对象
        CartInfo cartInfo2 = JSONObject.toJavaObject(jsonObject, CartInfo.class);
        //CartInfo(cattId=1001, statusCode=OK)
        System.out.println(cartInfo2);

    }
}
