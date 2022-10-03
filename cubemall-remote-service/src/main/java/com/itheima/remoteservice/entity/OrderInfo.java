package com.itheima.remoteservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OrderInfo {
    private Long orderNo;
    private String stateCode;
}
