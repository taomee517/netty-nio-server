package com.demo.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO改进案例
 *
 * @Author luotao
 * @E-mail taomee517@qq.com
 * @Date 2019\2\26 0026 22:25
 */
public class SuperBioDemo {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(20);
        try {
            ServerSocket server = new ServerSocket();
            server.bind(new InetSocketAddress(8001));
            while (true){
                Socket socket = server.accept();
                executor.execute(new ServerHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
