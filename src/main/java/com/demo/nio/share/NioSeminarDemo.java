package com.demo.nio.share;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * Nio share 案例
 *
 * @Author luotao
 * @E-mail taomee517@qq.com
 * @Date 2019\2\28 0028 22:43
 */
public class NioSeminarDemo {
    public static void main(String[] args) {
        ServerSocketChannel channel = null;
        Selector selector = null;
        try{
            InetSocketAddress address = new InetSocketAddress(8400);
            channel = ServerSocketChannel.open();
            channel.configureBlocking(false);
            channel.socket().bind(address);
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException ex){
            System.out.println("Couldn't setup server socket");
            System.out.println(ex.getMessage());
            System.exit(1);
        }
        ListenerThread listener = new ListenerThread(selector);
        listener.start();
    }
}
