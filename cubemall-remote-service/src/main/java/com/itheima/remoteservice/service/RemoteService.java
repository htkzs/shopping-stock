package com.itheima.remoteservice.service;

import com.alibaba.fastjson.JSONObject;
import com.itheima.remoteservice.entity.DeliverInfo;
import com.itheima.remoteservice.entity.GoodsInfo;
import com.itheima.remoteservice.entity.OrderInfo;
import com.itheima.remoteservice.entity.PointsInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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
}
