package com.itheima.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
//测试SocketBIO
public class SocketClient {
    public static void main(String[] args) throws IOException, IOException {
        Socket socket = new Socket("localhost", 9090);
        /*
         客户端以字节流的方式发送数据
         socket.getOutputStream().write("HelloServer".getBytes());
         socket.getOutputStream().flush();
         */
        //SocketClient.CharacterStream(socket);
        SocketClient.ByteStream(socket);
        System.out.println("向服务端发送数据结束");
        byte[] bytes = new byte[1024];
        //接收服务端回传的数据 这里阻塞等待服务端返回结果 目的让客户端不结束连接 否则服务端将报错 Connection reset
        socket.getInputStream().read(bytes);
        System.out.println("接收到服务端的数据：" + new String(bytes));
        //关闭流资源
    }
    //以字节流的方式向服务端发送数据
    public static void ByteStream(Socket socket) throws IOException {
        //服务端以 如果服务端以 in.read(bytes) 接收
        socket.getOutputStream().write("HelloServer".getBytes());
        socket.getOutputStream().flush();
        //socket.close();
    }
    //以字符流的方式发送数据
    public static void CharacterStream(Socket socket) throws IOException {
        //向服务端发送数据 如果服务端以 reader.readLine();字符流的方式接受 那么客户端就需要以 字符流的方式发送

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        //向服务端发送数据
        out.println("HelloServer");
        //out.close();
        //socket.close();
    }


}
