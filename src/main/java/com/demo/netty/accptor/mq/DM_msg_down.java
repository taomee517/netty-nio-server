/**
 *
 */
package com.demo.netty.accptor.mq;

import com.blackTea.common.constants.ClientTypeEnum;
import com.blackTea.common.model.Message;
import com.blackTea.util.log.LogUtil;
import com.demo.netty.accptor.config.AcceptorConfig;
import com.demo.netty.accptor.session.NettySession;
import io.netty.channel.Channel;
import mq.consumer.BaseMQMsgConsumer;
import mq.m.Msg;
import mq.m.MsgC;
import mq.m.MsgT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * domainModel☞消息下行
 * 从消息队列接收消息, 处理消息下行.
 *
 * @author yu.hou, email: houyujiangjun@qq.com
 * @date 2017年12月5日
 */
public class DM_msg_down extends BaseMQMsgConsumer {

    private static org.slf4j.Logger logger = LogUtil.getLogger(DM_msg_down.class);

    /**
     * 如果有监听的消息,需要记录
     *
     * @param msg
     * @param channel
     */
    private void recordWatchedMsg(Message msg, Channel channel) {
        try {
            if (null != msg.getWatchedMsg()) {
                NettySession.recordWatchedMsg2Session(channel, msg.getWatchedMsg());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 真实的业务处理,
     *
     * @return 是否处理成功
     */
    @Override
    public boolean consumeImpl(Msg m) {
        try {
            //logger.info("@@@下行消息<-{}", m);
            //尝试下行消息
            Object obj = m.getContent();
            if (obj instanceof Message) {
                Message msg = (Message) obj;
                Channel channel = NettySession.getChannelFromSession((msg).getEntityID());
                if (null != channel && channel.isActive()) {
                    //如果有监听的消息,需要记录
                    recordWatchedMsg(msg, channel);
                    //直接发送
                    channel.writeAndFlush(obj);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return true;
    }

    @Override
    public MsgC getConsumerID() {
//        return AcceptorConfig.instance().getMsgConsumer_down();
        return null;
    }

    /**
     * 关心下行消息
     */
    @Override
    public List<MsgT> getMsgTopicSet() {
        List<MsgT> msgTArr = new ArrayList<MsgT>();
        List<Integer> ports = Arrays.asList(ClientTypeEnum.OTU.getPort(), ClientTypeEnum.BSJ.getPort(), ClientTypeEnum.GL500.getPort());
        for(Integer port : ports){
            msgTArr.add(AcceptorConfig.instance(port).getMsgTopic_down());
        }
        return msgTArr;
    }
}
