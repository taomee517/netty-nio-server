package com.demo.nio.share.multi;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @Author luotao
 * @E-mail taomee517@qq.com
 * @Date 2019\3\1 0001 20:47
 */
public class Handler implements Runnable {
    final SocketChannel socket;
    final SelectionKey selectionKey;
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    boolean flag;

    Handler(Selector selector,SocketChannel channel) throws Exception{
        socket = channel;
        channel.configureBlocking(false);
        selectionKey = channel.register(selector,SelectionKey.OP_READ);
        selectionKey.attach(this);
        selector.wakeup();
    }

    boolean inputComplete(){
        return true;
    }

    boolean outputComplete(){
        return flag;
    }

    void process(){
        byte[] bytes = new byte[1024];
        for(int i = buffer.position();i<buffer.limit();i++){
            bytes[i] = buffer.get();
        }
        System.out.println(new String(bytes,Charset.forName("utf-8")));
        String out = "Handing Success!\n";
        buffer.clear();
        buffer.put(out.getBytes());
        flag = true;
    }

    @Override
    public void run() {
        try {
            if(selectionKey.isReadable()){
                read();
            }else if(selectionKey.isWritable()){
                send();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void read() throws Exception{
        buffer.clear();
        socket.read(buffer);
        buffer.flip();
        if(inputComplete()){
            process();
            selectionKey.interestOps(SelectionKey.OP_WRITE);
        }
    }

    public void send() throws Exception{
        buffer.flip();
        socket.write(buffer);
        if(outputComplete()){
            selectionKey.interestOps(SelectionKey.OP_READ);
        }
    }
}
