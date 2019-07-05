package com.demo.nio.apps;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class NioSelectorApp {
    public static void main(String[] args) throws Exception {
        List<Integer> ports = Arrays.asList(8001,8002,8003);
        Selector selector = Selector.open();
        for(Integer port:ports){
            try {
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                serverSocketChannel.configureBlocking(false);
                ServerSocket serverSocket = serverSocketChannel.socket();
                InetSocketAddress address = new InetSocketAddress(port);
                serverSocket.bind(address);
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                System.out.println("监听端口号：" + port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        while (true){
            int number = selector.select();
            System.out.println("创建连接数：" + number);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("SelectionKeys:" + selectionKeys);
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
               SelectionKey selectionKey = iterator.next();
               if(selectionKey.isAcceptable()){
                   ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                   SocketChannel socketChannel = serverSocketChannel.accept();
                   socketChannel.configureBlocking(false);
                   socketChannel.register(selector,SelectionKey.OP_READ);
                   iterator.remove();
                   System.out.println("获取客户端连接：" + socketChannel);
               }else if(selectionKey.isReadable()){
                   SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                   int index = 0;
                   while (true) {
                       ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                       byteBuffer.clear();
                       int read = socketChannel.read(byteBuffer);
                       if(read <=0){
                           break;
                       }
                       byteBuffer.flip();
                       socketChannel.write(byteBuffer);
                       index += read;
                   }
                   iterator.remove();
                   System.out.println("读取：" + index + "数据来自于：" + socketChannel);
               }
            }

        }

    }
}
