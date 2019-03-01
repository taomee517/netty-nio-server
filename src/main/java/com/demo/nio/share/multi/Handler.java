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
    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);
    static final int READING = 0;
    static final int SENDING = 1;
    int state = READING;
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
        String in = new String(input.array(), Charset.forName("utf-8"));
        System.out.println(in);
        String out = "Handing Success!";
        output.put(out.getBytes());
        flag = true;
    }

    @Override
    public void run() {
        try {
            if(state == READING){
                read();
            }else if(state == SENDING){
                send();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void read() throws Exception{
        socket.read(input);
        if(inputComplete()){
            process();
            state = SENDING;
            selectionKey.interestOps(SelectionKey.OP_WRITE);
        }
    }

    public void send() throws Exception{
        socket.write(output);
        if(outputComplete()){
            selectionKey.cancel();
        }
    }
}
