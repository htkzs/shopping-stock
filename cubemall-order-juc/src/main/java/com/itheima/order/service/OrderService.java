package com.itheima.order.service;

import com.alibaba.fastjson.JSONObject;
import com.itheima.order.entity.CartInfo;
import com.itheima.order.entity.GoodsInfo;
import com.itheima.order.entity.OrderInfo;
import com.itheima.order.feign.RemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Service
@Slf4j
public class OrderService {
    @Autowired
    private RemoteService remoteService;

    public Object order(long userId){
        //第一个执行过程交由主线程执行
        long startTime = System.currentTimeMillis();
        OrderInfo orderInfo = remoteService.createOrder(userId);
        String jsonString = JSONObject.toJSONString(orderInfo);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        System.out.println(orderInfo);
        System.out.println(jsonObject);
        //创建一个子线程执行该任务 由于需要返回值故 使用callable+futureTusk方式
        Callable<JSONObject> goodsCall = new Callable<JSONObject>() {
            @Override
            public JSONObject call() throws Exception {
                GoodsInfo goodsInfo = remoteService.dealGoods(orderInfo);
                String goodsJsonString = JSONObject.toJSONString(goodsInfo);
                JSONObject goodsString = JSONObject.parseObject(goodsJsonString);

                return goodsString;
            }
        };
        FutureTask<JSONObject> goodsFuture = new FutureTask<JSONObject>(goodsCall);
        Thread thread1 = new Thread(goodsFuture);

        Callable<JSONObject> pointsCall = new Callable<JSONObject>() {
            @Override
            public JSONObject call() throws Exception {
                JSONObject pointsJsonObject = remoteService.dealPoints(orderInfo);
                System.out.println(pointsJsonObject);
                return pointsJsonObject;
            }
        };
        FutureTask<JSONObject> pointFuture = new FutureTask<JSONObject>(pointsCall);
        Thread thread2 = new Thread(pointFuture);

        Callable<JSONObject> deliverCall = new Callable<JSONObject>() {
            @Override
            public JSONObject call() throws Exception {
                JSONObject deliverJsonObject = remoteService.dealDeliver(orderInfo);
                System.out.println(deliverJsonObject);
                return deliverJsonObject;
            }
        };
        FutureTask<JSONObject> deliverFuture = new FutureTask<JSONObject>(deliverCall);
        Thread thread3 = new Thread(deliverFuture);

        /*
         此处会阻塞主线程 等待结果返回
         */
        try {
            jsonObject.putAll(goodsFuture.get());
            jsonObject.putAll(pointFuture.get());
            jsonObject.putAll(deliverFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        /*
        这种转换方式会报错误
        String orderInfo=remoteService.createOrder(userId);
        JSONObject jsonObject1 = JSONObject.parseObject(orderInfo);
         */

//          String orderInfoString = JSON.toJSONString(orderInfo);
//          JSONObject jsonObject = JSONObject.parseObject(orderInfoString);
//        OrderInfo object = JSON.parseObject(orderInfo, OrderInfo.class);
//
//        //对结果进行聚合
//        orderInfo.putAll(goodsInfo);
//        orderInfo.putAll(pointsInfo);
//        orderInfo.putAll(deliverInfo);
//        System.out.println("id的值为"+userId+"===========orderInfo的值为"+orderInfo);
//        System.out.println("id的值为"+userId+"===========goodsInfo的值为"+goodsInfo);
//        System.out.println("id的值为"+userId+"===========pointsInfo的值为"+pointsInfo);
//        System.out.println("id的值为"+userId+"===========deliverInfo的值为"+deliverInfo);

        long endTime = System.currentTimeMillis();
        log.info("业务执行所需的总时间为："+(endTime - startTime)+"ms");
        return jsonObject;
    }

    public JSONObject JsonObjectOrder(long userId) {
        CartInfo cartInfo = new CartInfo(10001L,"OK");
        String toJSONString = JSONObject.toJSONString(cartInfo);
        System.out.println(toJSONString);
        JSONObject jsonObject = JSONObject.parseObject(toJSONString);
        return jsonObject;
    }
}
