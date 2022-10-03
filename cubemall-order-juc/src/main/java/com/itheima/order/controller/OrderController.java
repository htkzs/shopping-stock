package com.itheima.order.controller;

import com.itheima.order.entity.CartInfo;
import com.itheima.order.feign.RemoteService;
import com.itheima.order.service.CartService;
import com.itheima.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//需要以JSON格式返回数据到浏览器
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    //输入的参数格式为 order？userId=?
    @RequestMapping("/order")
    public Object orderInfo(@RequestParam("userId") long userId){
        return orderService.order(userId);
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
