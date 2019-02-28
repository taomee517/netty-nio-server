package com.demo.nio.share;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

/**
 * 事件监听线程
 *
 * @Author luotao
 * @E-mail taomee517@qq.com
 * @Date 2019\2\28 0028 22:40
 */
public class ListenerThread extends Thread {

    private Selector selector;

    public ListenerThread(Selector selector){
        this.selector = selector;
    }

    @Override
    public void run(){
        while (true){
            try{
                while (this.selector.select() > 0){
                    Set<SelectionKey> keys = this.selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()){
                            System.out.println("Accepting connection!");
                            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                            SocketChannel channel = serverChannel.accept();
                            channel.configureBlocking(false);
                            channel.register(this.selector, SelectionKey.OP_READ);
                        } else if (key.isReadable()){
                            System.out.println("Accpting command!");
                            SocketChannel channel = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(256);
                            channel.read(buffer);
                            buffer.flip();
                            Charset charset = Charset.forName("UTF-8");
                            CharsetDecoder decoder = charset.newDecoder();
                            CharBuffer charBuffer = decoder.decode(buffer);
                            System.out.println(charBuffer.toString());
                            channel.register(this.selector, SelectionKey.OP_WRITE);
                        } else if (key.isWritable()){
                            System.out.println("Sending response!");
                            SocketChannel channel = (SocketChannel) key.channel();
                            channel.write(ByteBuffer.wrap(new String("Hello World\n").getBytes()));
                            channel.register(this.selector, SelectionKey.OP_READ);
                        }
                        iterator.remove();
                    }
                }
            } catch (IOException ex){
                System.out.println("Error in poll loop");
                System.out.println(ex.getMessage());
                System.exit(1);
            }
        }
    }
}
