package com.demo.netty.accptor.handler;

import com.blackTea.common.communicate.netty.config.CommunicationConfig;
import com.blackTea.common.communicate.netty.model.NettyContext;
import com.blackTea.common.model.Message;
import com.blackTea.common.spi.SPIServiceFactory;
import com.blackTea.util.log.LogUtil;
import com.blackTea.util.serialize.SerializeUtil;
import com.demo.netty.accptor.config.AcceptorConfig;
import com.demo.netty.accptor.service.IDeviceConfigService;
import com.demo.netty.accptor.session.NettySession;
import com.google.common.collect.Lists;
import com.jfinal.kit.Kv;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.mortbay.util.ajax.JSON;
import org.slf4j.Logger;

import java.util.Map;

/**
 * @author yu.hou, email: houyujiangjun@qq.com
 * @date 2017年12月6日
 */
public class ClientLogoutHandler extends ChannelInboundHandlerAdapter {

    static Logger logger = LogUtil.getLogger(ClientLogoutHandler.class);

    /**
     * 需要考虑已经在使用其他channel连接
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        int port = NettySession.getRequestPortFromChannel(ctx.channel());
        logger.info("channelInactive-ctx:{}", JSON.toString(ctx));
        int clientType = NettySession.getClientTypeFromChannel(ctx.channel());
        int entityID = NettySession.getEntityIDFromChannel(ctx.channel());
        logger.info("离线事件,clienttype->{}, entityID->{}", clientType, entityID);
        //必须登录之后才能处理的逻辑
        if (entityID > 0) {
            //减一个数量
            AcceptorConfig.instance(port).getAcceptorStatusHandler().addConn();
            IDeviceConfigService service = SPIServiceFactory.findService(IDeviceConfigService.class,clientType);
            Message msg = new Message();
            msg.setClientType(clientType);
            msg.setEntityID(entityID);
            msg.setLevelcode(NettySession.getLevelCodeFromChannel(ctx.channel()));
            //被踢下去的处理是不同的
            Map<String, Boolean> map = Kv.by(CommunicationConfig.NETTY_SESSIONKEY_isReplaced, NettySession.getIsRemovedFromChannel(ctx.channel()));
            msg.setExtend(Lists.newArrayList(SerializeUtil.toByte(map)));
            service.onLogOut(msg);
            Channel channelNow = NettySession.getChannelFromSession(entityID);
            //是同一个
            if (null != channelNow && channelNow.equals(ctx.channel())) {
                NettySession.removeChannelFromSession(entityID);
            }
        }
        try {
            logger.info("设备{}与平台已断开连接!", NettySession.getImeiFromChannel(ctx.channel()));
            ctx.fireChannelInactive();
            NettyContext.removeAll(ctx.channel());
        } catch (Exception e) {
            logger.error("设备" + entityID + "处理断开链接失败", e);
        }
    }
}
