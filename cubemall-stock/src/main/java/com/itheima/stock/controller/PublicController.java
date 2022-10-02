package com.itheima.stock.controller;

import com.itheima.stock.service.DecStockNoLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/stock")
public class PublicController {
    @Autowired
    private DecStockNoLock decStockNoLock;
    @RequestMapping("/decStock")
    public String decStock(){
        decStockNoLock.decStockLock();
        return "success";
    }
}
