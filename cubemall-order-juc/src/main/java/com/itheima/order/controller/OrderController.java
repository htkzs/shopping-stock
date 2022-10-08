package com.itheima.order.controller;

import com.itheima.order.entity.CartInfo;
import com.itheima.order.feign.RemoteService;
import com.itheima.order.service.CartService;
import com.itheima.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

//需要以JSON格式返回数据到浏览器
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    /*
    不采用任何优化的原始方式
    接口测试lURL: http://localhost:8081/order1?userId=1;  测试业务执行所需的总时间为：41ms
     */
    //输入的参数格式为 order？userId=?
    @RequestMapping("/order1")
    public Object orderInfo1(@RequestParam("userId") long userId){
        return orderService.order1(userId);
    }

    /*
    此方式为不采用异步调用的方式 只使用多线程的方式 业务执行所需的总时间为：12ms
    接口测试lURL: http://localhost:8081/order?userId=1;
     */
    //输入的参数格式为 order？userId=?
    @RequestMapping("/order")
    public Object orderInfo(@RequestParam("userId") long userId){
        return orderService.order(userId);
    }

    //由于在进行结果聚合时 子线程将阻塞tomcat线程的执行 为了释放tomcat线程这里 优化为异步方式
    /*
    测试URL: http://localhost:8081/orderAsync?userId=1   测试业务执行所需的总时间为：386ms
    测试结果：
     */
    @RequestMapping("/orderAsync")
    public Callable<Object> orderInfoAsync(@RequestParam("userId") long userId) {
        Callable<Object> orderInfoCallable = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Object order = orderService.order(userId);
                return order;
            }
        };
        return orderInfoCallable;
    }

    //异步批量调用 减少后端接口的访问压力 分批次调用后端接口  而不是一个请求调用一次接口
    /*
    测试URL http://localhost:8081/orderAsyncForBatch?orderId=1
     */
    @RequestMapping("/orderAsyncForBatch")
    public Callable<Object> orderInfoAsyncForBatch(@RequestParam("orderId") long orderId) {
        Callable<Object> orderInfoCallable = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Object order = orderService.orderFastBatch(orderId);
                return order;
            }
        };
        return orderInfoCallable;
    }

    //直接返回对象到浏览器
    @RequestMapping("/cart")
    public CartInfo cartInfo(@RequestParam("cartId") long cartId){
        return cartService.createCart(cartId);
    }
    // 返回一个Object类型
    @RequestMapping("/cart/object")
    public Object cartInfoObject(@RequestParam("cartId") long cartId){
        return cartService.createCart(cartId);
    }
    @RequestMapping("/cart/jsonObject")
    public Object cartInfoJsonObject(@RequestParam("userId") long userId){
        return orderService.JsonObjectOrder(userId);
    }
}
