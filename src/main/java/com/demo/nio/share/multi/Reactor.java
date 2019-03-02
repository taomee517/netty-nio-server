package com.demo.nio.share.multi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * set up
 *
 * @Author luotao
 * @E-mail taomee517@qq.com
 * @Date 2019\3\1 0001 20:21
 */
public class Reactor implements Runnable {
    final Selector selector;
    final ServerSocketChannel channel;
    ExecutorService worker = Executors.newFixedThreadPool(4);


    public Reactor(int port) throws Exception{
        selector = Selector.open();
        channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        channel.socket().bind(new InetSocketAddress(port));
        SelectionKey sk = channel.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new Acceptor());
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                int c = selector.select();
                if(c<=0){
                    continue;
                }
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                while (it.hasNext()){
                    dispatch(it.next());
                    it.remove();
                }
             }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void dispatch(SelectionKey key){
        Runnable r = (Runnable) key.attachment();
        if(r!=null){
            r.run();
        }
    }

    class Acceptor implements Runnable {
        @Override
        public void run() {
            try {
                SocketChannel socketChannel = channel.accept();
                if(socketChannel!=null){
                    worker.execute( new Handler(selector,socketChannel));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
