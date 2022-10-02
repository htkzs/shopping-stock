package com.itheima.stock.lock;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "e")
public class TestSynchronizedBasicLock {
    @SneakyThrows
    public static void main(String[] args) {
        Lock lock = new Lock();
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                log.debug("Critical logic");
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                log.debug("Critical logic");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t2");
        t1.start();
        t2.start();
    }
}