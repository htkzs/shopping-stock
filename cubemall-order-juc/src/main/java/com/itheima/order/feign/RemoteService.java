package com.itheima.order.feign;

import com.alibaba.fastjson.JSONObject;
import com.itheima.order.entity.GoodsInfo;
import com.itheima.order.entity.OrderInfo;
import com.itheima.order.entity.PointsInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient("cubemall-remote-service")
public interface RemoteService {
    //对于方法传参 这里相当于反向传参
//    @RequestMapping("/create/order/{userId}")
//    public String createOrder(@PathVariable("userId") long userId);

    @RequestMapping("/create/order/{userId}")
    public OrderInfo createOrder(@PathVariable("userId") long userId);

    @RequestMapping("/create/goods")
    public GoodsInfo dealGoods(@RequestBody OrderInfo orderInfo);

    @RequestMapping("/create/points")
    public JSONObject dealPoints(@RequestBody OrderInfo orderInfo);

    @RequestMapping("/create/deliver")
    public JSONObject dealDeliver(@RequestBody OrderInfo orderInfo);
}
