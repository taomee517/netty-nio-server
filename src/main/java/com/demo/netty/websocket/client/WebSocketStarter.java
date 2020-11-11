package com.demo.netty.websocket.client;

/**
 * @author 罗涛
 * @title WebSocketStarter
 * @date 2020/11/11 17:02
 */
public class WebSocketStarter {
    static final String URL = System.getProperty("url", "ws://127.0.0.1:50055/ws");

    public static void main(String[] args) throws Exception {
        WebsocketClient websocketClient = new WebsocketClient();
        websocketClient.connect(URL);
    }
}
