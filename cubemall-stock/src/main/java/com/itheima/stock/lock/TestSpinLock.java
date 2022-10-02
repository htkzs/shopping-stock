package com.itheima.stock.lock;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
@Slf4j(topic = "e")
public class TestSpinLock {
    @SneakyThrows
    public static void main(String[] args) {
        ShadowLock lock = new ShadowLock();
        //线程t1
        Thread t1 = new Thread(() -> {
            log.debug("start execute");
            lock.lock();
            log.debug("Critical logic 1");
            try {
                //模拟一个超时操作
                TimeUnit.SECONDS.sleep(2);
                log.debug("Critical logic 2....other");
                TimeUnit.SECONDS.sleep(6);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();

            log.debug("thread end");
        }, "t1");
        //线程t2
        Thread t2 = new Thread(() -> {
            log.debug("start execute");
            lock.lock();
            log.debug("Critical logic");
            lock.unlock();
            log.debug("thread end");
        }, "t2");
        t1.start();
        //这个睡眠意义不大，主要保证t1能够在t2之前调度
        TimeUnit.MILLISECONDS.sleep(20);
        t2.start();
    }
}