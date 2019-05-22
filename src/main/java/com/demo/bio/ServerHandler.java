package com.demo.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * 线程处理器
 *
 * @Author luotao
 * @E-mail taomee517@qq.com
 * @Date 2019\2\26 0026 22:29
 */
public class ServerHandler implements Runnable {
    Socket socket;

    public ServerHandler() {
    }

    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            String msg = null;
            while((len = in.read(bytes))>0) {
                msg = new String(bytes, 0, len);
                System.out.println("收到客户端消息！" + msg);
                try {
                    Thread.sleep(90000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                out = socket.getOutputStream();
                out.write(msg.getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
