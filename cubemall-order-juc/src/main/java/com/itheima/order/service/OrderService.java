package com.itheima.order.service;

import com.alibaba.fastjson.JSONObject;
import com.itheima.order.entity.CartInfo;
import com.itheima.order.entity.GoodsInfo;
import com.itheima.order.entity.OrderInfo;
import com.itheima.order.feign.RemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

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
        thread1.start();

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
        thread2.start();
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
        thread3.start();
        /*
         get()此处会阻塞主线程 等待结果返回 tomcat线程会在这里阻塞等待结果的response
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

    public Object order1(long userId){
        //第一个执行过程交由主线程执行
        long startTime = System.currentTimeMillis();
        OrderInfo orderInfo = remoteService.createOrder(userId);
        String jsonString = JSONObject.toJSONString(orderInfo);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        System.out.println(orderInfo);
        System.out.println(jsonObject);

        GoodsInfo goodsInfo = remoteService.dealGoods(orderInfo);
        String goodsJsonString = JSONObject.toJSONString(goodsInfo);
        JSONObject goodsString = JSONObject.parseObject(goodsJsonString);
        System.out.println(goodsString);

        JSONObject pointsJsonObject = remoteService.dealPoints(orderInfo);
        System.out.println(pointsJsonObject);

        JSONObject deliverJsonObject = remoteService.dealDeliver(orderInfo);
        System.out.println(deliverJsonObject);

        jsonObject.putAll(goodsString);
        jsonObject.putAll(pointsJsonObject);
        jsonObject.putAll(deliverJsonObject);
        long endTime = System.currentTimeMillis();
        log.info("业务执行所需的总时间为："+(endTime - startTime)+"ms");
        return jsonObject;
    }




    //声明一个阻塞队列 后期可以使用MQ中间件代替
    LinkedBlockingQueue<Request> queue = new LinkedBlockingQueue<Request>();
    //调用后端微服务批量处理
    public JSONObject orderFastBatch(long orderId) throws ExecutionException, InterruptedException {
        //1.处理订单
        JSONObject orderInfo = remoteService.createOrderFast(orderId);

        System.out.println("获取到的OrderInfo的信息为："+orderInfo);
        log.info("获取到的OrderInfo的信息为：{}",orderInfo);
        //2.处理库存等其它信息
        CompletableFuture<JSONObject> future = new CompletableFuture<JSONObject>();
        //orderInfo重复 分布式ID的处理方案
        String uuid = UUID.randomUUID().toString();
        orderInfo.put("uuid",uuid);
        Request request = new Request();
        request.object = orderInfo;
        request.future = future;

        //将单个的请求收集在一个阻塞队列中
        queue.add(request);
        //阻塞 等结果，批量调用数据分发的结果
        return future.get();
    }
    //定义一个请求信息的封装
    class Request{
        //存放请求需要的参数对象
        JSONObject object;
        //多线程进行异步结果的处理 和FutureTusk相似
        CompletableFuture<JSONObject> future;
    }

    //创建bean的同时构建一个 定时任务的线程池
    @PostConstruct
    public void doBusiness(){
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);
        threadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                int size = queue.size();
                if(size == 0){
                    return;
                }
                if(size < 10){
                    return;
                }
                //批量接口调用
                List<JSONObject> orderList = new ArrayList<>();
                //批量封装请求
                List<Request> requestList = new ArrayList<>();
                for (int i = 0; i < size ; i++) {
                    Request request = queue.poll();
                    orderList.add(request.object);
                    requestList.add(request);
                }
                //批量接口调用
                List<JSONObject> resultList = remoteService.dealGoodsFastBatch(orderList);
                System.out.println("批量接口调用一次：数据打包的数量---"+size);
                //根据响应找对应的请求
                for(JSONObject result:resultList){
                    for(Request request : requestList){
                        String request_uuid = request.object.getString("uuid");
                        String result_uuid = result.getString("uuid");
                        if(request_uuid.equals(result_uuid)){
                            //把结果返回到  future.get();获取结果的阻塞处。
                            request.future.complete(result);
                            break;
                        }
                    }
                }
            }
        },1000,50,TimeUnit.MILLISECONDS);
    }
    public JSONObject JsonObjectOrder(long userId) {
        CartInfo cartInfo = new CartInfo(10001L,"OK");
        String toJSONString = JSONObject.toJSONString(cartInfo);
        System.out.println(toJSONString);
        JSONObject jsonObject = JSONObject.parseObject(toJSONString);
        return jsonObject;
    }
}
