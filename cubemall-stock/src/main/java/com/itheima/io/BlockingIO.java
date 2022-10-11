package com.itheima.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class BlockingServer {
    public static void main(String[] args) throws Exception {
        //创建线程池
        ExecutorService threadPool = Executors.newCachedThreadPool();
        //创建Socket 9999端口
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("服务端:服务端启动成功，线程信息 ID="+Thread.currentThread().getId()+" Name="+Thread.currentThread().getName());
        while (true){
            //死循环监听客户端连接
            System.out.println("服务端:阻塞，等待新客户端连接");
            final Socket socket = serverSocket.accept();
            //创建一个线程和客户端通信
            threadPool.execute(new Runnable() {
                public void run() {
                    System.out.println("服务端:连接到一个客户端，线程信息 ID="+Thread.currentThread().getId()+" Name="+Thread.currentThread().getName());
                    handler(socket);
                }
            });
        }
    }

    //一个Handler方法用于处理客户端消息
    public static void handler(Socket socket){
        try {
            //创建一个byte数组用于接受数据
            byte[] bytes = new byte[1024];
            //从socket获取当前客户端的流
            InputStream inputStream = socket.getInputStream();
            //循环读取客户端的流
            while (true){
                System.out.println("服务端:阻塞，等待客户端发送数据，线程信息 ID="+Thread.currentThread().getId()+" Name="+Thread.currentThread().getName());
                int read = inputStream.read(bytes);
                //如果read不是-1表示有数据 把当前读取的内容打印出来
                if (read != -1){
                    System.out.println("接受到客户端消息:"+new String(bytes,0,read));
                }else {//否则就退出循环
                    break;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("服务端:关闭和client的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

