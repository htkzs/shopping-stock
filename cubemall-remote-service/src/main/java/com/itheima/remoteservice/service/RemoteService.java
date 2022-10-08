package com.itheima.remoteservice.service;

import com.alibaba.fastjson.JSONObject;
import com.itheima.remoteservice.entity.DeliverInfo;
import com.itheima.remoteservice.entity.GoodsInfo;
import com.itheima.remoteservice.entity.OrderInfo;
import com.itheima.remoteservice.entity.PointsInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class RemoteService {
    public OrderInfo createOrder(Long userId){
        return new OrderInfo(1000L,"OK");
    }
    public GoodsInfo dealGoods(@RequestBody OrderInfo orderInfo){
        return new GoodsInfo(1001L,"OK");
    }
    public PointsInfo dealPoints(@RequestBody OrderInfo orderInfo){
        return new PointsInfo(1002L,"OK");
    }
    /*
    注意controller层增加@RequestBody service层也需要增加 @RequestBody
     */
    public DeliverInfo dealDeliver(@RequestBody OrderInfo orderInfo){
        return new DeliverInfo(1003L,"OK");
    }

    //批量处理操作
    public JSONObject createOrderFast(Long orderId){
        System.out.println("获取到调用的参数orderId的值为："+orderId);
        OrderInfo orderInfo = new OrderInfo(1004L,"OK");
        String jsonString = JSONObject.toJSONString(orderInfo);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        return jsonObject;
    }
    //批量的接口调用
    public List<JSONObject> delGoodsFastBatch(List<JSONObject> orderInfoList){
        List<JSONObject> goodsObjectList = new ArrayList<JSONObject>();
        for (JSONObject orderInfo:orderInfoList) {
            System.out.println("传入的参数orderInfo为："+orderInfo);
            JSONObject goodsInfo = this.dealGoodsOne(orderInfo);
            goodsObjectList.add(goodsInfo);
        }
        return goodsObjectList;
    }

    private JSONObject dealGoodsOne(JSONObject orderInfo) {
        GoodsInfo goodsInfo = new GoodsInfo(1008L,"OK");
        String goodsJSON = JSONObject.toJSONString(goodsInfo);
        JSONObject goodsJSONObject = JSONObject.parseObject(goodsJSON);
        return goodsJSONObject;


    }
}
