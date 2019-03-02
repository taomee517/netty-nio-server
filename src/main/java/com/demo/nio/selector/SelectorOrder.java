package com.demo.nio.selector;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Set;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/2/21
 * @time 10:54
 */
public class SelectorOrder {

    public static void main(String... args) throws Exception {
        ServerSocketChannel channel =  ServerSocketChannel.open();
        channel.configureBlocking(false);
        ServerSocket socket = channel.socket();
        InetSocketAddress address = new InetSocketAddress(8001);
        socket.bind(address);
        Selector selector = Selector.open();
        SelectionKey sk =channel.register(selector, SelectionKey.OP_ACCEPT);
        while (true){
            int selectSize = selector.select();
            if(selectSize == 0){
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Set<SelectionKey> allKeys = selector.keys();
            selectionKeys.forEach(selectionKey -> {
                final SocketChannel client;
                try{
                    if(selectionKey.isAcceptable()){
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
                        client = serverSocketChannel.accept();
                        client.configureBlocking(false);
                        client.register(selector,SelectionKey.OP_READ);
                        System.out.println("accept");
                    }else if(selectionKey.isReadable()){
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
                        while (true) {
                            int read = socketChannel.read(byteBuffer);
                            if(read <=0){
                                break;
                            }
                        }
                        byteBuffer.flip();
                        String out = new String(byteBuffer.array());
                        socketChannel.write(byteBuffer);
                        System.out.println("read");
                    }else if(selectionKey.isWritable()){
                        System.out.println("write");
                    }else if(selectionKey.isConnectable()){
                        System.out.println("connect");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }finally {

                }
            });
            selectionKeys.clear();
        }
    }
}
