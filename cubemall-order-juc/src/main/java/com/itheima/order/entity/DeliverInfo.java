package com.itheima.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliverInfo {
    private long deliverId;
    private String deliverCode;
}
