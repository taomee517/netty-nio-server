package com.demo.bio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * 单线程BIO案例
 *
 * @Author luotao
 * @E-mail taomee517@qq.com
 * @Date 2019\2\26 0026 21:46
 */
public class BioDemo {
    public static void main(String[] args) {
        InputStream in = null;
        OutputStream out = null;
        try {
            ServerSocket server = new ServerSocket();
            server.bind(new InetSocketAddress(8001));
            Socket socket = server.accept();
            while (true) {
                in = socket.getInputStream();
                if(in.available()>0){
                    byte[] bytes = new byte[512];
                    in.read(bytes);
                    String msg = new String(bytes, Charset.forName("utf-8"));
                    System.out.println("收到客户端消息！" + msg);
                    //如果IO是一个非常耗时的操作，则无法处理并发访问
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    out = socket.getOutputStream();
                    out.write(msg.getBytes());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
