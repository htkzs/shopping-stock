package com.itheima.io;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

//BIO 同步阻塞模型
public class BIOServer {
    private ServerSocket socket;//开发工具包ServerSocket
    private Scanner scanner;
    private final int port = 5676;
    private BufferedWriter writer;
    private BufferedReader reader;
    private OutputStream outputStream;
    private InputStream inputStream;
    public BIOServer() {
        try {
            socket = new ServerSocket(port);//绑定端口
            scanner = new Scanner(System.in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //服务器端的业务逻辑  此方法由于没有创建新的线程所以只能连接一个客户端
    public void startTCPServer(){

        try {
            Socket clientSocket = socket.accept();//会发生阻塞，阻塞到连接建立完成。连接建立，TCP三次握手的终点
            while(true){
                //accept()拿到返回值clientSocket 连接建立成功
                outputStream = clientSocket.getOutputStream();//发送信息
                inputStream = clientSocket.getInputStream();//接收信息
                //字节转字符 非必须
                writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String s = reader.readLine(); //阻塞等待客户端发送消息
                System.out.println("接收用户的信息:" + s);
                if (s.equals("exit")) {
                    break;
                }
                System.out.println("回复：");
                String s1 = scanner.nextLine();
                writer.write(s1 +"\n");
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(socket!=null){
                try {
                    socket.close();
                    reader.close();
                    writer.close();
                    outputStream.close();
                    inputStream.close();
                    socket =null;
                    reader =null;
                    writer =null;
                    outputStream=null;
                    inputStream=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
        new BIOServer().startTCPServer();
    }
}
