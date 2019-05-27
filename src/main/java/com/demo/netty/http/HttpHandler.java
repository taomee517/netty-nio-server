package com.demo.netty.http;

import com.demo.netty.basic.constant.DefaultValue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.net.SocketAddress;

/**
 * Netty Http案例客户端请求处理类
 *
 * @author luotao
 * @e-mail taomee517@qq.com
 * @date 2019\1\27
 * @time 13:02
 */
public class HttpHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        SocketAddress addr = ctx.channel().remoteAddress();
        System.out.println(addr);
        System.out.println(msg.getClass());
        if(msg instanceof HttpRequest){
            HttpRequest request = (HttpRequest)msg;
            String uri = request.uri();
            HttpMethod method = request.method();
            System.out.println(uri);
            System.out.println(method);
            ByteBuf content = Unpooled.copiedBuffer(DefaultValue.DEFAULT_FIRST_MSG,DefaultValue.DEFAULT_CHARSET);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
            System.out.println("say hello to client");
            ctx.writeAndFlush(response);
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handler added");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel registered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel active");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel inactive");
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel unregistered");
        super.channelUnregistered(ctx);
    }
}
