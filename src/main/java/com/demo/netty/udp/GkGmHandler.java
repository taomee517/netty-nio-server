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
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @author 罗涛
 * @title GkGmHandler
 * @date 2020/11/24 15:45
 */
@Slf4j
public class GkGmHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    //累积数据
//    public static final String realMsg = "FE00000001000240D910988000FF03C07FFE1D002509DC400001000A0005000C00000000453964EE45774A577FFE00422509DC400001000A00050010000000004075AC9541CB907A461600007FFE1D00250A14800001000A0005000C000000004539634A457747347FFE0042250A14800001000A00050010000000004075ADCE41CD14CE461600007FFE1D00250A4CC00001000A0005000C00000000453960B9457748967FFE0042250A4CC00001000A00050010000000004075D0114210EB17461600007FFE1D00250A85000001000A0005000C000000004539651C4577459E7FFE0042250A85000001000A00050010000000004075D6D0421E438E461600007FFE1D00250ABD400001000A0005000C0000000045396231457749A47FFE0042250ABD400001000A00050010000000004075D36C41F7907D461600007FFE1D00250AF5800001000A0005000C0000000045395EAA457748287FFE0042250AF5800001000A00050010000000004075D12241DD08AE461600007FFE1D00250B2DC00001000A0005000C00000000453960A9457749787FFE0042250B2DC00001000A0005001000000000407585F741D43E74461600007FFE1D00250B66000001000A0005000C0000000045395E73457749337FFE0042250B66000001000A000500100000000040758F0A41D86ED4461600007FFE1D00250B9E400001000A0005000C0000000045395CCC457743AF7FFE0042250B9E400001000A00050010000000004075A35742031728461600007FFE1D00250BD6800001000A0005000C000000004539613245773D7E7FFE0042250BD6800001000A00050010000000004075AD4F421C0DCC461600007FFE1D00250C0EC00001000A0005000C0000000045396133457739A77FFE0042250C0EC00001000A00050010000000004075A5A941E7F6A6461600007FFE1D00250C47000001000A0005000C0000000045395F2D45773C8E7FFE0042250C47000001000A00050010000000004075AA6441D88882461600007FFE1D00250C7F400001000A0005000C000000004539619045774F697FFE0042250C7F400001000A000500100000000040755C1641D5826D461600007FFE1D00250CB7800001000A0005000C000000004539609E4577463F7FFE0042250CB7800001000A0005001000000000407567CA41D90684461600007FFE1D00250CEFC00001000A0005000C00000000453960D045773E317FFE0042250CEFC00001000A000500100000000040758D7142376211461600007FFE1D00250D28000001000A0005000C000000004539664A457732677FFE0042250D28000001000A0005001000000000407584324232FA614616000063AA";
    //单条数据
//    public static final String realMsg = "FE0000000100023E7310988000FF004C7FFE1D0027595F980001000A0005001C0000000044238E3E459708614429D3A34595BF01440E78B545973A177FFE004227595F980001000A00050010000000004086F76D418DD8D0461600005970";
    public static final String realMsg = "fe 00 00 00 01 00 02 3e 73 10 98 80 00 ff 00 4c 7f fe 1d 00 27 65 36 10 00 01 00 0a 00 05 00 1c 00 00 00 00 fe 96 76 99 45 98 44 b0 44 2d 4a bd 45 98 2f 2e 44 1e 48 cc 45 97 84 24 7f fe 00 42 27 65 36 10 00 01 00 0a 00 05 00 10 00 00 00 00 40 86 e8 86 41 91 da c4 46 16 00 00 b8 a6";

    //设备编号设置回复 CRC换位
//    public static final String realMsg = "FE 00 00 00 02 00 00 12 34 00 18 80 00 FF 00 14 7F FE 00 47 00 0E 77 90 00 04 00 07 00 00 00 04 00 00 00 00 F3 30";
    //设备编码查询回复
//    public static final String realMsg = "FE 00 00 00 01 7F FF FF FF 00 18 80 00 FF 00 18 7F FE 00 47 1B E2 56 73 00 04 00 05 00 00 00 08 00 00 00 00 00 00 12 34 19 49";
    //校时查询结果 CRC换位
//    public static final String realMsg = "FE 00 00 00 01 00 00 12 34 00 18 80 00 FF 00 18 7F FE 00 44 1B 99 A9 76 00 04 00 05 00 00 00 08 00 00 00 00 1B 99 A9 76 0B CB";
    //数据中心配置回复
//    public static final String realMsg = "FE 00 00 00 01 00 00 12 34 00 18 80 00 00 00 18 7F FE 08 01 1B E2 3F B6 00 04 00 07 00 00 00 08 00 00 00 00 00 00 00 00 D5 5E";
    //杜撰错误数据
//    public static final String realMsg = "FE0000000100023E7310988000FF004C7FFE1D0027595F980001000A0005001C0000000044238E3E459708614429D3A34595BF01440E78B5FE967699FEFE004227595F980001000A00050010000000004086F76D418DD8D0461600007D38";





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
    public static void sendMessage(String msg, final InetSocketAddress inetSocketAddress, Channel channel){
        if(msg == null){
            throw new NullPointerException("msg is null");
        }
        // TODO 这一块的msg需要做处理 字符集转换和Bytebuf缓冲区
        msg = StringUtils.replace(msg, " ", "");
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


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
        IdleState state = idleStateEvent.state();
        switch (state) {
            case READER_IDLE:
            case WRITER_IDLE:
                break;
            case ALL_IDLE:
                sendMessage(realMsg, inetSocketAddress, ctx.channel());
                break;
        }
    }
}
