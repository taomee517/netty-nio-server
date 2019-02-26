package com.demo.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO改进案例
 *
 * @Author luotao
 * @E-mail taomee517@qq.com
 * @Date 2019\2\26 0026 22:25
 */
public class JuniorBioDemo {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket();
            server.bind(new InetSocketAddress(8001));
            while (true){
                Socket socket = server.accept();
                new Thread(new ServerHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
