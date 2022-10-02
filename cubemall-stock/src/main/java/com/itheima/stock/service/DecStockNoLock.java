package com.itheima.stock.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

//模拟多线程的减库存的超卖问题
@Service
public class DecStockNoLock {
    // 初始化Logger类:使用指定的类初始化 LoggerFactory，输出日志所在类的信息。
    private static Logger logger = LoggerFactory.getLogger(DecStockNoLock.class);
    //定义一个临界资源 多线程操作不安全
    Integer stock;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String decStockLock(){
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select stock from shop_order where id =1");
        if(result == null || (stock = (Integer) result.get(0).get("stock"))<=0){
            logger.info("下单失败，已尽没有库存了");
            return "下单失败，已尽没有库存了";
        }
        stock--;
        jdbcTemplate.update("update shop_order set stock=? where id =1",stock);
        logger.info("下单成功，当前产品剩余：--------->"+ stock);
        return "下单成功，当前产品剩余：--------->"+ stock;
    }

}
