package com.demo.netty.http;

import com.demo.netty.basic.constant.DefaultValue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;

import static com.demo.netty.basic.constant.DefaultValue.DEFAULT_CHARSET;
import static com.demo.netty.basic.constant.DefaultValue.DEFAULT_FIRST_MSG;
import static com.demo.netty.basic.constant.DefaultValue.NOT_FOUND;

/**
 * Netty Http案例客户端请求处理类
 *
 * @author luotao
 * @e-mail taomee517@qq.com
 * @date 2019\1\27
 * @time 13:02
 */
@Slf4j
public class HttpHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        SocketAddress addr = ctx.channel().remoteAddress();
        log.info("请求地址：{}",addr);
        log.info("请求消息类：{}",msg.getClass());
        if(msg instanceof HttpRequest && "/".equals(((HttpRequest) msg).uri())){
            HttpRequest request = (HttpRequest)msg;
            String uri = request.uri();
            HttpMethod method = request.method();
            log.info("请求路径：{}",uri);
            log.info("请求方法：{}",method);
            ByteBuf content = Unpooled.copiedBuffer(DEFAULT_FIRST_MSG, DEFAULT_CHARSET);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
            log.info("say hello to client");
            ctx.writeAndFlush(response);
        }else {
            ByteBuf content = Unpooled.copiedBuffer(NOT_FOUND,DEFAULT_CHARSET);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.NOT_FOUND,content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
            log.info("can not find url");
            ctx.writeAndFlush(response);
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("handler added");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channel registered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel active");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel inactive");
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channel unregistered");
        super.channelUnregistered(ctx);
    }
}
