package com.itheima.order.fastjson;

import com.alibaba.fastjson.JSONObject;
import com.itheima.order.service.OrderService;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class orderInfoAsyncForBatchTest {
    @Autowired
    private OrderService orderService;
    private static final Logger logger = LoggerFactory.getLogger(orderInfoAsyncForBatchTest.class);
    //定义压测的并发量
    private static final int THREAD_NUM = 10000;
    private CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);
    public void testInterface(){
        for (int i = 0; i < THREAD_NUM; i++) {
            final long orderId = 100+i;
            Thread thread = new Thread(()->{

                try {
                    //计数 最准计数到0 所有阻塞的线程将会同时运行。
                    countDownLatch.countDown();
                    countDownLatch.await();
                    JSONObject jsonObject = orderService.orderFastBatch(orderId);
                    System.out.println(jsonObject);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

            });
            thread.start();
        }

    }

}
