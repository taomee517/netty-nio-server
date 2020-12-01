package com.demo.netty.udp;

import com.demo.netty.echo.utils.BytesUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @author 罗涛
 * @title GkGmHandler
 * @date 2020/11/24 15:45
 */
@Slf4j
public class GkGmHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    public static final String realMsg = "FE00000001000240D910988000FF03C07FFE1D002509DC400001000A0005000C00000000453964EE45774A577FFE00422509DC400001000A00050010000000004075AC9541CB907A461600007FFE1D00250A14800001000A0005000C000000004539634A457747347FFE0042250A14800001000A00050010000000004075ADCE41CD14CE461600007FFE1D00250A4CC00001000A0005000C00000000453960B9457748967FFE0042250A4CC00001000A00050010000000004075D0114210EB17461600007FFE1D00250A85000001000A0005000C000000004539651C4577459E7FFE0042250A85000001000A00050010000000004075D6D0421E438E461600007FFE1D00250ABD400001000A0005000C0000000045396231457749A47FFE0042250ABD400001000A00050010000000004075D36C41F7907D461600007FFE1D00250AF5800001000A0005000C0000000045395EAA457748287FFE0042250AF5800001000A00050010000000004075D12241DD08AE461600007FFE1D00250B2DC00001000A0005000C00000000453960A9457749787FFE0042250B2DC00001000A0005001000000000407585F741D43E74461600007FFE1D00250B66000001000A0005000C0000000045395E73457749337FFE0042250B66000001000A000500100000000040758F0A41D86ED4461600007FFE1D00250B9E400001000A0005000C0000000045395CCC457743AF7FFE0042250B9E400001000A00050010000000004075A35742031728461600007FFE1D00250BD6800001000A0005000C000000004539613245773D7E7FFE0042250BD6800001000A00050010000000004075AD4F421C0DCC461600007FFE1D00250C0EC00001000A0005000C0000000045396133457739A77FFE0042250C0EC00001000A00050010000000004075A5A941E7F6A6461600007FFE1D00250C47000001000A0005000C0000000045395F2D45773C8E7FFE0042250C47000001000A00050010000000004075AA6441D88882461600007FFE1D00250C7F400001000A0005000C000000004539619045774F697FFE0042250C7F400001000A000500100000000040755C1641D5826D461600007FFE1D00250CB7800001000A0005000C000000004539609E4577463F7FFE0042250CB7800001000A0005001000000000407567CA41D90684461600007FFE1D00250CEFC00001000A0005000C00000000453960D045773E317FFE0042250CEFC00001000A000500100000000040758D7142376211461600007FFE1D00250D28000001000A0005000C000000004539664A457732677FFE0042250D28000001000A0005001000000000407584324232FA614616000063AA";
    private static InetSocketAddress inetSocketAddress = new InetSocketAddress(DefaultValues.HOST, DefaultValues.PORT);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        sendMessage(realMsg, inetSocketAddress, ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket packet) throws Exception {
        // TODO 不确定服务端是否有response 所以暂时先不用处理
        final ByteBuf buf = packet.content();
        int readableBytes = buf.readableBytes();
        byte[] content = new byte[readableBytes];
        buf.readBytes(content);
        String serverMessage = new String(content);
        log.info("receive server msg is: {}", serverMessage);
    }

    /**
     * 向服务器发送消息
     * @param msg 按规则拼接的消息串
     * @param inetSocketAddress 目标服务器地址
     */
    public static void sendMessage(final String msg,final InetSocketAddress inetSocketAddress, Channel channel){
        if(msg == null){
            throw new NullPointerException("msg is null");
        }
        // TODO 这一块的msg需要做处理 字符集转换和Bytebuf缓冲区
        send(datagramPacket(msg, inetSocketAddress), channel);
    }

    public static void send(final DatagramPacket datagramPacket, Channel channel){
        channel.writeAndFlush(datagramPacket);
    }


    /**
     * 组装数据包
     * @param msg 消息串
     * @param inetSocketAddress 服务器地址
     * @return DatagramPacket
     */
    private static DatagramPacket datagramPacket(String msg, InetSocketAddress inetSocketAddress){
//        ByteBuf dataBuf = Unpooled.copiedBuffer(msg, Charset.forName("UTF-8"));
        byte[] bytes = BytesUtil.hex2Bytes(msg);
        ByteBuf dataBuf = Unpooled.wrappedBuffer(bytes);
        DatagramPacket datagramPacket = new DatagramPacket(dataBuf, inetSocketAddress);
        return datagramPacket;
    }


//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
//        IdleState state = idleStateEvent.state();
//        switch (state) {
//            case READER_IDLE:
//            case WRITER_IDLE:
//                break;
//            case ALL_IDLE:
//                sendMessage(realMsg, inetSocketAddress, ctx.channel());
//                break;
//        }
//    }
}
