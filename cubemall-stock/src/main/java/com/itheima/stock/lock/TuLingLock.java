package com.itheima.stock.lock;

import org.springframework.objenesis.instantiator.util.UnsafeUtils;
import sun.misc.Unsafe;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
//CAS+自旋+LockSupport
public class TuLingLock{
    //定义一个标志位用于CAS检查
    private volatile int state = 0;
    //标志锁资源的持有者
    private Thread lockHolder;
    //定义一个线程安全的queue 不能用阻塞队列(基于AQS)
    private final ConcurrentLinkedQueue<Thread> waiters = new ConcurrentLinkedQueue<Thread>();

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Thread getLockHolder() {
        return lockHolder;
    }

    public void setLockHolder(Thread lockHolder) {
        this.lockHolder = lockHolder;
    }

    //封装一个方法执行尝试获取锁
    public boolean acquire(){
        int state = getState();
        //第一次进来判断 state=0 表示可以加锁
        if(state == 0){
            //如果当前锁对象中的 state记录的为0 代表可以加锁 并将状态改为1 方便下一个线程执行CAS操作
            if((waiters.size() == 0 || Thread.currentThread() == waiters.peek()) && compareAndSwapState(0,1)){
                //锁资源被当前线程持有
                setLockHolder(Thread.currentThread());
                return true;
            }
        }
        return false;
    }

    public void lock() {
        if(acquire()){
            //加锁成功
            return;
        }
        //加锁不成功的操作 线程进入队列等待
        Thread current = Thread.currentThread();
        waiters.add(current);
        //空转浪费资源
        for(;;){
            //peek() 只取第一个元素 ，不会移除队头 这里体现的是公平锁
            if(waiters.peek() == current && acquire()){
                //加锁成功 移除队头
                 waiters.poll();
                 return;
            }
            //阻塞当前线程 等待上一个线程释放锁后 唤醒当前线程
            LockSupport.park();
        }
    }

    public void unlock() throws Exception {
        //首先判断当前线程是否持有锁 对不持有锁的线程进行释放锁操作将抛出异常
        if(Thread.currentThread() == lockHolder){
            //通过CAS修改锁状态
            compareAndSwapState(1,0);
            //修改当前线程不在持有锁
            setLockHolder(null);
            //取出队头
            if(waiters.peek() != null){
               //t1释放锁 唤醒t2 相当于释放断点 再次尝试加锁
                LockSupport.unpark(Thread.currentThread());
            }
        }else{
            throw new RuntimeException("lockHolder is not current");
        }
    }
    //获取unsafe对象
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    //需要进行CAS原子操作检查，检查当前锁状态是否被线程持有
    private static long stateOffset;
    public final Boolean compareAndSwapState(int except,int update){
        return unsafe.compareAndSwapInt(this,stateOffset,except,update);
    }

    static{
        try {
            stateOffset = unsafe.objectFieldOffset(TuLingLock.class.getDeclaredField("state"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
