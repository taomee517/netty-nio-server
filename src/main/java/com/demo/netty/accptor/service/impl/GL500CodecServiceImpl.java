/**
 *
 */
package com.demo.netty.accptor.service.impl;

import com.alibaba.fastjson.JSON;
import com.blackTea.common.communicate.netty.model.NettyContext;
import com.blackTea.common.model.Message;
import com.blackTea.common.model.TV;
import com.blackTea.util.DateTimeUtil;
import com.blackTea.util.log.LogUtil;
import com.demo.netty.accptor.constant.GL500Constants;
import com.demo.netty.accptor.service.IByteToNettyMsgCodecService;
import com.demo.netty.accptor.session.NettySession;
import com.demo.netty.accptor.util.AckHandle;
import com.demo.netty.accptor.util.RespHandle;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ByteProcessor;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import static com.demo.netty.accptor.constant.GL500Constants.*;

/**
 *
 *
 *
 */
public class GL500CodecServiceImpl implements IByteToNettyMsgCodecService {
    private static org.slf4j.Logger logger = LogUtil.getLogger(GL500CodecServiceImpl.class);


    @Override
    public String encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        if (msg.getFunction() == null) {
            return null;
        }

        String function = new String(msg.getFunction());

        if (GL500Constants.AT.equals(function)) {
            String downContent = "";
            if (msg.getTvList().size() >= 1) {
                logger.info("同时下发命令条数={}", msg.getTvList().size());
                for (TV tv :
                        msg.getTvList()) {
                    logger.info("tv={}", JSON.toJSONString(tv));
                    String tag = new String(tv.getStrTag());
                    String value = new String(tv.getStrValue());
                    downContent = downContent + MessageFormat.format(GL500Constants.AT_FORMAT, tag, value);
                }
            }
            logger.info("downContent==={}", downContent);
            //写入才能发送
            out.writeBytes(downContent.getBytes());
            return downContent;
        }

        return null;
    }

    @Override
    public String decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //之前的处理方式，没有对TCP流做任何处理
//        byte[] data = new byte[in.readableBytes()];
//        in.readBytes(data);
//        String upMsg = new String(data);

        String upMsg = getUniqueFullMsg(in);
        logger.info("upMsg====={}", upMsg);
        if (StringUtils.isNotEmpty(upMsg)) {
            //实例：+RESP:GTALS,400300,868487003716602,GL500,SRI,3,,1,gl500.mysirui.com,2107,223.6.250.102,2107,,0,0,0,,,,20161027164723,02A4$
            //设备主动上报
            if (RespHandle.isResp(upMsg)) {
                new RespHandle().handleResp(ctx, out, upMsg);
                //设备回应
            } else if (AckHandle.isAck(upMsg)) {
                new AckHandle().handleResp(ctx, out, upMsg);
            }
        }
        //每2分钟，设置一条虚拟心跳，用于触发任务机制(@not 其实最好是用定时器的)
        createVirtualHeart2Busi(ctx, out);
        return upMsg;
    }

    private void createVirtualHeart2Busi(ChannelHandlerContext ctx, List<Object> out) {
        Object value = NettyContext.get(ctx.channel(), GL500Constants.VIRTUAL_HEART_KEY);

        if (value == null) {
            NettyContext.put(ctx.channel(), GL500Constants.VIRTUAL_HEART_KEY, System.currentTimeMillis());
        } else {
            //判断是否超过2分钟
            long time = (long) value;
            logger.info("设备{}检测虚拟心跳任务的生成，缓存时间为{}，当前时间为{}", NettySession.getEntityIDFromChannel(ctx.channel()), DateTimeUtil.getDateTime(new Date(time)), DateTimeUtil.getDateTime(new Date()));
            if (System.currentTimeMillis() - time >= GL500Constants.VIRTUAL_HEART_KEY_INTERVAL) {
                logger.info("设备{}产生虚拟心跳任务", NettySession.getEntityIDFromChannel(ctx.channel()));
                Message message = new Message();
                //用于返回那边的encode的处理
                message.setFunction((RESP_PREFIX + GL500Constants.VIRTUAL_HEART_KEY).getBytes());
                message.addTv(new TV(GL500Constants.VIRTUAL_HEART_KEY.getBytes()));
                out.add(message);

                NettyContext.put(ctx.channel(), GL500Constants.VIRTUAL_HEART_KEY, System.currentTimeMillis());
            }
        }
    }

    /**
     * TCP流的拆包处理
     * @param in tcp流
     * @return tcp流中第一条完整的报文，如果没有返回null
     */
    private static String getUniqueFullMsg(ByteBuf in){
        int startSignIndex = in.forEachByte(new ByteProcessor.IndexOfProcessor(START_SIGN));
        if(startSignIndex==-1){
            return null;
        }
        in.readerIndex(startSignIndex);
        int endSignIndex = in.forEachByte(new ByteProcessor.IndexOfProcessor(END_SIGN));
        if(endSignIndex == -1 || endSignIndex < startSignIndex){
            return null;
        }
        int length = endSignIndex - startSignIndex + 1;
        byte[] data = new byte[length];
        in.readBytes(data,0,length);
        String resultTemp = new String(data);
        if(!resultTemp.startsWith(ACK_PREFIX) && !resultTemp.startsWith(RESP_PREFIX)){
            return null;
        }
        return resultTemp;
    }

}
