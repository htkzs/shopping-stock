package com.itheima.order.service;

import com.itheima.order.entity.CartInfo;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    public CartInfo createCart(long cartId){
        return new CartInfo(10001L,"OK");
    }
}
