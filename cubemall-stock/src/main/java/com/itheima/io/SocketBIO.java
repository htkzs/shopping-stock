package com.itheima.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
/*
BIO通信模型带来的问题是
1.每一个连接都要创建一个线程
2.C10K个连接将消耗大量的内存（需要分配给线程栈）对硬件要求较高
3.不是每个连接建立后就会迅速发送数据 导致资源占用


linux相关调试
netstat -natp 查看活动的TCP网络连接（服务端监听 建立连接的）
strace -ff -o out
 */
public class SocketBIO {
    private static final Logger logger = LoggerFactory.getLogger(SocketBIO.class);

    public static void main(String[] args) throws Exception {

        /*
        程序员new ServerSocket() 对应系统的三个调用 socket bind listen 只要一建立连接的数据就会保存在recv-q中
         */
        ServerSocket server = new ServerSocket(9090,20);  //netstat -natp   listen socket

        System.out.println("step1: new ServerSocket(9090) ");

        while (true) {
            Socket client = server.accept();  //阻塞1
            System.out.println("step2:client\t" + client.getPort());
            //这里创建一个子线程执行获取客户端发送的数据的目的在于，使得服务端可以接受其它的连接请求 ，而不是阻塞主线程导致不能接受其它的客户端请求。
            /*
            clone系统调用创建线程 线程栈独立 堆共享
             */
            //接受客户端传递的字符流
//            new Thread(new Runnable(){
//                public void run() {
//                    InputStream in = null;
//                    try {
//                        in = client.getInputStream();
//
//                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//                        while(true){
//                            String dataline = reader.readLine(); //阻塞等待接受客户端发送的数据
//                            if(null != dataline){
//                                System.out.println(dataline);
//                            }else{
//                                client.close();
//                                break;
//                            }
//                        }
//                        System.out.println("客户端断开");
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
            // 接受客户端传递的字节流
            new Thread(new Runnable(){
                public void run() {
                    InputStream in = null;
                    try {
                        in = client.getInputStream();
                        //定义一个缓冲区
                        byte[] bytes = new byte[1024];
                        while(true){
                            int dataline =in.read(bytes); //阻塞等待接受客户端发送的数据
                            if(dataline != -1){
                                System.out.println("接受到客户端消息:"+new String(bytes,0,dataline));
                            }else{
                                client.close();
                                break;
                            }
                        }
                        System.out.println("客户端断开");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        }
    }


}
