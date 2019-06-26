/**
 *
 */
package com.demo.netty.accptor.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.blackTea.common.communicate.netty.model.NettyContext;
import com.blackTea.common.model.Message;
import com.blackTea.common.model.TV;
import com.blackTea.common.ops.ComponentEnum;
import com.blackTea.util.BytesUtil;
import com.blackTea.util.log.LogUtil;
import com.demo.netty.accptor.constant.BSJConstant;
import com.demo.netty.accptor.model.BSJCodecResult;
import com.demo.netty.accptor.service.IByteToNettyMsgCodecService;
import com.demo.netty.accptor.session.NettySession;
import com.demo.netty.accptor.util.BSJCodecUtil;
import com.fzk.logger.flume.TerminalFlumeLog;
import com.fzk.sdk.util.BytesTranUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.List;

import static com.demo.netty.accptor.constant.BSJConstant.MIN_LENTH;
import static com.demo.netty.accptor.constant.BSJConstant.VIRTUAL_HEART_KEY;

/**
 * @author yu.hou
 * 注意fst框架的缺陷, 无法保证消息完整性,需要用编解码器来实现完整的消息流读取.
 */
@Slf4j
public class BSJCodecServiceImpl implements IByteToNettyMsgCodecService {
    private static Logger flumeLogger = LogUtil.getAcceessFlumeOutLog();

    @Override
    public String encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        byte[] result = BSJCodecUtil.serialize(msg);
        String str = BytesUtil.bytesToHexString4BSJ(result);
        //		logger.info("应答消息<-{}", BytesUtil.bytesToHexString(result));
        out.writeBytes(result);//再写序列化之后的消息本身.
        return str;
    }

    @Override
    public String decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            String str = printReqData(in);
            String imei = NettySession.getImeiFromChannel(ctx.channel());
            int clientType = NettySession.getClientTypeFromChannel(ctx.channel());
            log.info("bsj-iemi--{}---收到消息str-{}", imei, str);
            TerminalFlumeLog flumeLog = new TerminalFlumeLog(BytesTranUtil.hexStringToBytes(StringUtils.replace(str, " ", "")), ComponentEnum.acceptor_bsj.getAppName(), imei, clientType);
            flumeLogger.info(JSONObject.toJSONString(flumeLog));
            if(str!=null) {
                byte[] resultArr = BytesTranUtil.hexStringToBytes(StringUtils.replace(str," ","" ));
                ByteBuf tempBuf = Unpooled.copiedBuffer(resultArr);
                List<BSJCodecResult> results = BSJCodecUtil.deSerialize(tempBuf);
                if (null != results && results.size() > 0) {
                    for (BSJCodecResult codecResult : results) {
                        if (null != codecResult.getMsg()) {
                            out.add(codecResult.getMsg());
                        }
                    }
                }
                createVirtualHeart2Busi(ctx, out);
            }

            return str;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            //			resetBuffer(ctx, in);//必须要把写的指针移到一个缓冲区的开始位置,不然不能保证一个完整的消息.
            in.clear();
        }
    }

    private String printReqData(ByteBuf in) {
        //78 78 12 13 EB 0B 04 04 04 F6 02 05 17 03 06 FF FB 00 6A 02 DA 0D 0A
        int length = in.readableBytes();
        if(length<MIN_LENTH){
            return null;
        }
        int readStart = in.readerIndex();
        int startIndex = -1;
        int endIndex = -1;
        byte lastByte = in.readByte();
        byte currentByte = 0;
        byte[] signBytes = null;
        for (int index = 0; index < length-1; index++) {
            currentByte = in.readByte();
            signBytes = new byte[]{lastByte,currentByte};
            if(BytesUtil.equals(signBytes, BSJConstant.START_POSITION)){
                startIndex = index;
            }else if(startIndex>=0 && BytesUtil.equals(signBytes, BSJConstant.STOP_POSITION)){
                endIndex = index + 1;
                int contentLength = endIndex - startIndex + 1;
                in.readerIndex(readStart + startIndex);
                byte[] result = new byte[contentLength];
                in.readBytes(result, 0, contentLength);
                String msg = BytesUtil.bytesToHexString4BSJ(result);
                return msg;
            }
            lastByte = currentByte;
        }
        if (startIndex >= 0) {
            in.readerIndex(readStart + startIndex);
        }
        return null;
    }

    private void createVirtualHeart2Busi(ChannelHandlerContext ctx, List<Object> out) {
        Object value = NettyContext.get(ctx.channel(), VIRTUAL_HEART_KEY);

        if (value == null) {
            NettyContext.put(ctx.channel(), VIRTUAL_HEART_KEY, System.currentTimeMillis());
        } else {
            //判断是否超过2分钟
            long time = (long) value;
            if (System.currentTimeMillis() - time >= 2 * 60 * 1000) {
                Message message = new Message();
                //用于返回那边的encode的处理
                message.setFunction(VIRTUAL_HEART_KEY.getBytes());
                message.addTv(new TV(VIRTUAL_HEART_KEY.getBytes()));
                out.add(message);

                NettyContext.put(ctx.channel(), VIRTUAL_HEART_KEY, System.currentTimeMillis());
            }
        }
    }
}
