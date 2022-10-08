package com.itheima.remoteservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.itheima.remoteservice.entity.DeliverInfo;
import com.itheima.remoteservice.entity.GoodsInfo;
import com.itheima.remoteservice.entity.OrderInfo;
import com.itheima.remoteservice.entity.PointsInfo;
import com.itheima.remoteservice.service.RemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//这里只是为了模拟多次远程调用而已
@RestController
@RequestMapping("/create")
public class RemoteController {
    @Autowired
    public RemoteService remoteService;
    @RequestMapping("/order/{userId}")
//    public String createOrder(@PathVariable("userId") long userId){
//        OrderInfo orderInfo = remoteService.createOrder(userId);
//        return orderInfo.toString();
//    }
    public OrderInfo createOrder(@PathVariable("userId") long userId){
        OrderInfo orderInfo = remoteService.createOrder(userId);
        return orderInfo;
    }
    @RequestMapping("/goods")
    public GoodsInfo dealGoods(@RequestBody OrderInfo orderInfo){
        GoodsInfo goodsInfo= remoteService.dealGoods(orderInfo);
        return goodsInfo;
    }
    @RequestMapping("/points")
    public JSONObject dealPoints(@RequestBody OrderInfo orderInfo){
        PointsInfo pointsInfo = remoteService.dealPoints(orderInfo);
        String jsonString = JSONObject.toJSONString(pointsInfo);
        JSONObject dealPointsObject = JSONObject.parseObject(jsonString);
        return dealPointsObject;
    }
    @RequestMapping("/deliver")
    public JSONObject dealDeliver(@RequestBody OrderInfo orderInfo){
        DeliverInfo deliverInfo = remoteService.dealDeliver(orderInfo);
        String jsonString = JSONObject.toJSONString(deliverInfo);
        JSONObject dealDeliverObject = JSONObject.parseObject(jsonString);
        return dealDeliverObject;
    }

    //根据orderId获取OrderInfo信息
    @RequestMapping("/order/batch/{orderId}")
    public JSONObject createOrderFast(@PathVariable long orderId){
        return remoteService.createOrderFast(orderId);
    }
    //批量远程接口调用
    @RequestMapping("/goods/batch")
    public List<JSONObject> delGoodsFastBatch(@RequestBody List<JSONObject> orderInfoList){
        return remoteService.delGoodsFastBatch(orderInfoList);
    }

}
