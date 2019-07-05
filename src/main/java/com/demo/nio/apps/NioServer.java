package com.demo.nio.apps;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;
/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date  2018/12/22
 * @time  11:14
 */
public class NioServer {
    public static Map<String,SocketChannel> clientMap =  new HashMap<>();

    public static void main(String... args) throws Exception {
        ServerSocketChannel channel =  ServerSocketChannel.open();
        channel.configureBlocking(false);
        ServerSocket socket = channel.socket();
        InetSocketAddress address = new InetSocketAddress(7788);
        socket.bind(address);
        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_ACCEPT);
        while (true){
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            selectionKeys.forEach(selectionKey -> {
                final SocketChannel client;
                try{
                    if(selectionKey.isAcceptable()){
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
                        client = serverSocketChannel.accept();
                        client.configureBlocking(false);
                        client.register(selector,SelectionKey.OP_READ);
                        String key = "【"+ UUID.randomUUID().toString() +"】";
                        clientMap.put(key,client);
                    }else if(selectionKey.isReadable()){
                        client = (SocketChannel)selectionKey.channel();
                        client.configureBlocking(false);
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int position = client.read(byteBuffer);
                        if(position>0){
                            byteBuffer.flip();
                            Charset charset = Charset.forName("UTF-8");
                            String message = String.valueOf(charset.decode(byteBuffer).array());
                            System.out.println("收到客户端消息：" + message);
                        }
                        String clientId = null;
                        for(Map.Entry<String,SocketChannel> entry:clientMap.entrySet()){
                            if(entry.getValue().equals(client)){
                                clientId = entry.getKey();
                            }
                        }
                    }
                    selectionKeys.clear();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {

                }
            });
        }
    }



}

