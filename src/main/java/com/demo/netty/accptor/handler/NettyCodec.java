///**
// *
// */
//package com.demo.netty.accptor.handler;
//
//import com.alibaba.fastjson.JSON;
//import com.blackTea.common.constants.ClientTypeEnum;
//import com.blackTea.common.constants.EventConstant;
//import com.blackTea.common.dataCollector.model.ClientLog;
//import com.blackTea.common.model.Message;
//import com.blackTea.common.spi.SPIServiceFactory;
//import com.blackTea.util.AddressUtil;
//import com.demo.netty.accptor.service.IByteToNettyMsgCodecService;
//import com.demo.netty.accptor.service.IDeviceConfigService;
//import com.demo.netty.accptor.session.NettySession;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.handler.codec.ByteToMessageCodec;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.net.SocketAddress;
//import java.util.List;
//
///**
// * @author yu.hou
// */
//@Slf4j
//public class NettyCodec extends ByteToMessageCodec<Message> {
//    private static Logger terminalLogger = LoggerFactory.getLogger("terminal-upside-down");
//    public static final String ADDRESS_CONNECTOR = ":";
//
//    @Override
//    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
//        SocketAddress localAddress = ctx.channel().localAddress();
//        int reqPort = Integer.valueOf(localAddress.toString().split(ADDRESS_CONNECTOR)[1]);
//        log.info("encode--channel服务端地址：{}", localAddress);
//        log.info("encode--channel客户端地址：{}",ctx.channel().remoteAddress());
//        int clientType = ClientTypeEnum.getEnumByPort(reqPort).getClientType();
//        IByteToNettyMsgCodecService servcie = SPIServiceFactory.findService(IByteToNettyMsgCodecService.class,clientType);
//        String str = servcie.encode(ctx, msg, out);
//        //记录日志到下行消息
//        if (null != str) {
//            int entityID = NettySession.getEntityIDFromChannel(ctx.channel());
//            String imei = NettySession.getImeiFromChannel(ctx.channel());
//            ClientLog log = new ClientLog(clientType, entityID,imei, EventConstant.MSG_DOWN, str);
//            log.setServer(AddressUtil.getLanIPCache());
//            terminalLogger.info(JSON.toJSONString(log));
//        }
//    }
//
//    @Override
//    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        SocketAddress serverAddr = ctx.channel().localAddress();
//        SocketAddress clientAddr = ctx.channel().remoteAddress();
//        log.info("decode--channel服务端地址：{}", serverAddr);
//        log.info("decode--channel客户端地址：{}",clientAddr);
//        log.info("channel id: {}", ctx.channel().id().asLongText());
//        int reqPort = Integer.valueOf(serverAddr.toString().split(ADDRESS_CONNECTOR)[1]);
//        NettySession.recordRequestPort2Channel(ctx.channel(),reqPort);
//        int clientType = ClientTypeEnum.getEnumByPort(reqPort).getClientType();
//        IByteToNettyMsgCodecService servcie = SPIServiceFactory.findService(IByteToNettyMsgCodecService.class,clientType);
//        String str = servcie.decode(ctx, in, out);
//        //记录日志到上行消息
//        log.info("记录到上行消息：{}", str);
//        if (null != str) {
//            int entityID = NettySession.getEntityIDFromChannel(ctx.channel());
//            if (entityID != 0) {
//                String imei = NettySession.getImeiFromChannel(ctx.channel());
//                ClientLog log = new ClientLog(clientType, entityID,imei, EventConstant.MSG_UP, str);
//                log.setServer(AddressUtil.getLanIPCache());
//                terminalLogger.info(JSON.toJSONString(log));
//            //有可能是登录或者寻址消息
//            } else {
//                try {
//                    if (out.size() > 0) {
//                        Message msg = (Message) out.get(0);
//                        IDeviceConfigService cofigSPIService = SPIServiceFactory.findService(IDeviceConfigService.class,clientType);
//                        //记录到session中.登录完成再记录
//                        if (cofigSPIService.isAddressMsg(msg) || cofigSPIService.isLoginMsg(msg)) {
//                            NettySession.recordCurrentTextMsg2Channel(ctx.channel(), str);
//                        }
//                    }
//                } catch (Exception e) {
//                    log.error(e.getMessage(), e);
//                }
//            }
//        }
//    }
//}
