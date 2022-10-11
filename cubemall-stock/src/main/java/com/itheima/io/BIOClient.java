package com.itheima.io;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class BIOClient {
    private Socket socket;//开发工具包
    private Scanner scanner;
    private final int port = 5676;
    private final String IP="127.0.0.1";
    private BufferedWriter writer;
    private BufferedReader reader;
    public BIOClient() {
        try {
            socket = new Socket(IP,port);//请求和服务器建立连接 三次握手的起点
            //在端口port=5676上打开与IP="127.0.0.1"的连接
            scanner = new Scanner(System.in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void startBIOClient(){
        //接收客户端的请求  字节流方向
        try {
            //socket建立成功 连接建立成功
            boolean closed = socket.isClosed();
            if(!closed){
                while(true){

                    OutputStream outputStream = socket.getOutputStream();//发送信息
                    InputStream inputStream = socket.getInputStream();//接收信息

                    //字节转字符 非必须
                    writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    System.out.println("向服务器端发送请求：");

                    String s1 = scanner.nextLine();
                    writer.write(s1 +"\n");
                    writer.flush();
                    if(s1.equals("exit")){
                        break;
                    }
                    String s = reader.readLine();
                    System.out.println("接收服务器的信息:" + s);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(socket!=null){
                try {
                    socket.close();
                    reader.close();
                    writer.close();
                    socket =null;
                    reader =null;
                    writer =null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new BIOClient().startBIOClient();
    }
}

