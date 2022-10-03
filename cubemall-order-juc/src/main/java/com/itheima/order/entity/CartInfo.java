package com.itheima.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartInfo {
    private long cattId;
    private String statusCode;
}
