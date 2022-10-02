package com.itheima.stock.lock;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "e")
public class ShadowLock {
    volatile int state = 0;//标识---是否有线程在同步块-----是否有线程上锁成功
    private static long stateOffset;//state这个字段在ShadowLock对象当中的内存偏移量
    private static Unsafe unsafe; //unsafe 对象，因为cas操作需要这个对象 可以直接越过JVM操作底层
    /*
     * 1、获取unsafe对象，这个对象的获取稍微有点复杂， 目前不需要关心
     * 2、通过unsafe对象得到state属性在当前对象当的偏移地址
     */
    static {
        try {
            unsafe = getUnSafe();
            stateOffset = unsafe.objectFieldOffset
                    (ShadowLock.class.getDeclaredField("state"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //获取unsafe对象
    public static Unsafe getUnSafe() throws NoSuchFieldException,
            IllegalAccessException {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        return (Unsafe) theUnsafe.get(null);
    }


    //cas修改state的值
    //expect预期值，如果相同则把state的值改成x 原子操作
    public boolean compareAndSet(int expect, int x) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, x);
    }
    //加锁方法
    @SneakyThrows
    public void lock() {
        //compareAndSet方法如果返回false 加锁失败整个while->true //compareAndSet方法如果返回true 加锁成功整个while->false
        while(!compareAndSet(0,1)){//加锁失败
            log.debug("lock fail");
            //这个睡眠是没有什么意义的，只是视觉效果好一点
            TimeUnit.MILLISECONDS.sleep(300);
        }
        //加锁成功
        log.debug("lock success");
}
    public void unlock(){
        log.debug("unlock success");
        //思考这里为什么不要cas?
        state=0;
    }
}
