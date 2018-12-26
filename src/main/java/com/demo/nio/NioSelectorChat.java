package com.demo.nio;

import sun.misc.VM;

import java.io.IOException;
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
import java.util.*;

public class NioSelectorChat {
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
                System.out.println("可连接端口：" + port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        while (true){
            int number = selector.select();
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
                       Charset charset = Charset.forName("UTF-8");
                       CharsetDecoder decoder = charset.newDecoder();
                       CharBuffer charBuffer = decoder.decode(byteBuffer.asReadOnlyBuffer());
                       String output = charBuffer.toString();
                       System.out.println("客户端消息:" + output);
                       index += read;
                   }
                   iterator.remove();
                   Scanner scanner = new Scanner(System.in);
                   String input = scanner.nextLine();
                   byte[] msg = ("服务器消息:" + input + "\n").getBytes();
                   ByteBuffer msgBuffer = ByteBuffer.allocate(1024);
                   for(int i=0;i<msg.length;i++){
                       msgBuffer.put(msg[i]);
                   }
                   msgBuffer.flip();
                   socketChannel.write(msgBuffer);
                   VM.isBooted();
               }
            }
        }
    }
}
