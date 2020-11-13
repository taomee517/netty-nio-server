package com.demo.netty.websocket.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * websocket服务器消息处理类
 *
 * @Author luotao
 * @E-mail taomee517@qq.com
 * @Date 2019\1\27 0027 19:21
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame webSocketFrame) throws Exception {
        String text = webSocketFrame.text();
        System.out.println("收到客户端消息：" + text);
//        String content = "服务接收消息时间：" + LocalTime.now();
//        TextWebSocketFrame resp = new TextWebSocketFrame(text);
//        channelHandlerContext.writeAndFlush(resp);
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            IdleState state = idleStateEvent.state();
            switch (state) {
                case READER_IDLE:
                    break;
                case WRITER_IDLE:
                    System.out.println("发送消息！");
                    String jsonData = "{\"mType\":16,\"sType\":7,\"pID\":197,\"sID\":27140,\"data\":[{\"Sec\":1597371594,\"mSec\":433,\"data\":5.5},{\"Sec\":1597371595,\"mSec\":708,\"data\":5.4600000381469727},{\"Sec\":1597371595,\"mSec\":644,\"data\":-4.4000000953674316},{\"Sec\":1597371597,\"mSec\":27,\"data\":5.320000171661377},{\"Sec\":1597371596,\"mSec\":876,\"data\":-4.4000000953674316},{\"Sec\":1597371598,\"mSec\":223,\"data\":5.3600001335144043},{\"Sec\":1597371597,\"mSec\":881,\"data\":-4.4000000953674316},{\"Sec\":1597371599,\"mSec\":521,\"data\":5.3000001907348633},{\"Sec\":1597371598,\"mSec\":966,\"data\":-4.4000000953674316},{\"Sec\":1597371600,\"mSec\":686,\"data\":5.28000020980835},{\"Sec\":1597371600,\"mSec\":108,\"data\":-4.3899998664855957},{\"Sec\":1597371601,\"mSec\":917,\"data\":5.1599998474121094},{\"Sec\":1597371601,\"mSec\":254,\"data\":-4.3899998664855957},{\"Sec\":1597371602,\"mSec\":284,\"data\":-4.3899998664855957},{\"Sec\":1597371603,\"mSec\":337,\"data\":5.2399997711181641}]}";
                    TextWebSocketFrame resp = new TextWebSocketFrame(jsonData);
                    ctx.writeAndFlush(resp);
                    break;
                case ALL_IDLE:
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handler added:" + ctx.channel().id().asShortText());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handler active");
//        String jsonData = "{\"mType\":16,\"sType\":7,\"pID\":197,\"sID\":27140,\"data\":[{\"Sec\":1597371594,\"mSec\":433,\"data\":5.5},{\"Sec\":1597371595,\"mSec\":708,\"data\":5.4600000381469727},{\"Sec\":1597371595,\"mSec\":644,\"data\":-4.4000000953674316},{\"Sec\":1597371597,\"mSec\":27,\"data\":5.320000171661377},{\"Sec\":1597371596,\"mSec\":876,\"data\":-4.4000000953674316},{\"Sec\":1597371598,\"mSec\":223,\"data\":5.3600001335144043},{\"Sec\":1597371597,\"mSec\":881,\"data\":-4.4000000953674316},{\"Sec\":1597371599,\"mSec\":521,\"data\":5.3000001907348633},{\"Sec\":1597371598,\"mSec\":966,\"data\":-4.4000000953674316},{\"Sec\":1597371600,\"mSec\":686,\"data\":5.28000020980835},{\"Sec\":1597371600,\"mSec\":108,\"data\":-4.3899998664855957},{\"Sec\":1597371601,\"mSec\":917,\"data\":5.1599998474121094},{\"Sec\":1597371601,\"mSec\":254,\"data\":-4.3899998664855957},{\"Sec\":1597371602,\"mSec\":284,\"data\":-4.3899998664855957},{\"Sec\":1597371603,\"mSec\":337,\"data\":5.2399997711181641}]}";
//        TextWebSocketFrame resp = new TextWebSocketFrame(jsonData);
//        ctx.writeAndFlush(resp);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception{
        System.out.println("handler removed:" + ctx.channel().id().asShortText());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        System.out.println("exception caught:" + ctx.channel().id().asShortText());
        cause.printStackTrace();
        ctx.close();
    }
}
