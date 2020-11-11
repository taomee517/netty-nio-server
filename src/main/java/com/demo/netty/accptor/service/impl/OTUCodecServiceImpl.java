///**
// *
// */
//package com.demo.netty.accptor.service.impl;
//
//import com.alibaba.fastjson.JSONObject;
//import com.blackTea.common.constants.ClientTypeEnum;
//import com.blackTea.common.model.Message;
//import com.blackTea.common.ops.ComponentEnum;
//import com.blackTea.util.StrUtil;
//import com.blackTea.util.log.LogUtil;
//import com.demo.netty.accptor.model.MsgSerializedResult;
//import com.demo.netty.accptor.service.IByteToNettyMsgCodecService;
//import com.demo.netty.accptor.session.NettySession;
//import com.demo.netty.accptor.util.OTUCodecUtil;
//import com.fzk.logger.flume.TerminalFlumeLog;
//import com.fzk.otu.utils.CRCUtil;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//
//import java.util.List;
//
///**
// * @author yu.hou
// * 注意fst框架的缺陷, 无法保证消息完整性,需要用编解码器来实现完整的消息流读取.
// */
//@Slf4j
//public class OTUCodecServiceImpl implements IByteToNettyMsgCodecService {
//    private static Logger flumeLogger = LogUtil.getAcceessFlumeOutLog();
//
//    @Override
//    public String encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
//        String result = OTUCodecUtil.serialize(msg);
//        log.info("应答消息,{}->{}", NettySession.getEntityIDFromChannel(ctx.channel()), result);
//        String encryptedResult = result;
//        //是加密消息
//        if (NettySession.getIsEncryptMsgFromChannel(ctx.channel())) {
//            encryptedResult = CRCUtil.asiccCrcEncode(result);
//        }
//        //再写序列化之后的消息本身.
//        out.writeBytes(StrUtil.joint("(", encryptedResult, ")").getBytes());
//        return result;
//    }
//
//    @Override
//    public String decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        try {
//            String msgStr = OTUCodecUtil.Byte2StringSerialize(in);
//            //要处理心跳.
//            if (null == msgStr) {
//                return null;
//            } else if (msgStr.length() == 0) {
//                out.add(new Message());
//                return "";
//            }
//            //是否是加密消息
//            boolean isEncryptMsg = msgStr.startsWith("*");
//            NettySession.recordEncryptMsgFromChannel(ctx.channel(), isEncryptMsg);
//            if (isEncryptMsg) {
//                log.info("收到消息密文,{}<-{}", NettySession.getEntityIDFromChannel(ctx.channel()), msgStr);
//            }
//            MsgSerializedResult result = OTUCodecUtil.deSerialize(msgStr);
//            if (null != result) {
//                log.info("收到消息,{}<-{}", NettySession.getEntityIDFromChannel(ctx.channel()), result.getPlainMsg());
//                String imei = NettySession.getImeiFromChannel(ctx.channel());
//                if (StringUtils.isEmpty(imei) && msgStr.contains("101") ) {
//                    //主要是为了让其登录时上报的命令传能上传到flume中
//                    imei = StringUtils.substringBetween(msgStr, "|101,", "|");
//                    if (msgStr.contains("|a3|")) {
//                        log.info("登录消息--imei:{}", imei);
//                    }
//                    if(msgStr.contains("|a1|")){
//                        log.info("寻址消息--imei:{}", imei);
//                    }
//                }
//                int clientType = NettySession.getClientTypeFromChannel(ctx.channel());
//                if (clientType <= 0) {
//                    //主要是为了让其登录时上报的命令传能上传到flume中
//                    clientType = ClientTypeEnum.OTU.getClientType();
//                }
//                TerminalFlumeLog flumeLog = new TerminalFlumeLog(msgStr.getBytes(),ComponentEnum.acceptor_otu_sirui.getAppName(),imei,clientType);
//				flumeLogger.info(JSONObject.toJSONString(flumeLog));
//                Message msg = result.getMsg();
//                out.add(msg);
//                return result.getPlainMsg();
//            } else {
//                return null;
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            return null;
//        } finally {
//            //必须要把写的指针移到一个缓冲区的开始位置,不然不能保证一个完整的消息.
//            resetBuffer(ctx, in);
//        }
//    }
//}
