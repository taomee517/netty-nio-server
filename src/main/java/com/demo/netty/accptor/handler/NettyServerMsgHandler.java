///**
// *
// */
//package com.demo.netty.accptor.handler;
//
//
//import com.blackTea.common.communicate.netty.config.CommunicationConfig;
//import com.blackTea.common.communicate.netty.model.NettyContext;
//import com.blackTea.common.constants.ClientTypeEnum;
//import com.blackTea.common.constants.EventConstant;
//import com.blackTea.common.dataCollector.model.ClientLog;
//import com.blackTea.common.model.Message;
//import com.blackTea.common.model.TV;
//import com.blackTea.common.model.WatchedMsg;
//import com.blackTea.common.spi.SPIServiceFactory;
//import com.blackTea.util.AddressUtil;
//import com.blackTea.util.StrUtil;
//import com.blackTea.util.log.LogUtil;
//import com.blackTea.util.serialize.SerializeUtil;
//import com.demo.netty.accptor.config.AcceptorConfig;
//import com.demo.netty.accptor.service.IDeviceConfigService;
//import com.demo.netty.accptor.session.NettySession;
//import com.google.common.collect.Lists;
//import com.jfinal.kit.Kv;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//import io.netty.handler.timeout.IdleState;
//import io.netty.handler.timeout.IdleStateEvent;
//import kv.m.KV_Client;
//import mq.consumer.ConsumerConfig;
//import mq.m.MsgB;
//import mq.m.MsgT;
//import mq.producer.MsgProducer;
//import org.mortbay.util.ajax.JSON;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @author yu.hou, email: houyujiangjun@qq.com
// * @date 2017年11月25日
// */
//public class NettyServerMsgHandler extends SimpleChannelInboundHandler<Message> {
//
//    private static Logger logger = LogUtil.getLogger(NettyServerMsgHandler.class);
//    private static Logger terminalLogger = LoggerFactory.getLogger("terminal-upside-down");
//    private IDeviceConfigService service;
//
//    public static String getValue(String tag, Message msg) {
//        Iterator var2 = msg.getTvList().iterator();
//
//        TV tv;
//        do {
//            if (!var2.hasNext()) {
//                return null;
//            }
//
//            tv = (TV) var2.next();
//        } while (!tag.equalsIgnoreCase(tv.getStrTag()));
//
//        return tv.getStrValue();
//    }
//
//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
//        ConcurrentHashMap<String, Object> map = ctx.channel().attr(CommunicationConfig.SESSIONKEY).get();
//        logger.info("session map:" + String.valueOf(map == null) + (map == null ? "" : JSON.toString(map)));
//        ctx.channel().id().asShortText();
//        int clientType = NettySession.getClientTypeFromChannel(ctx.channel());
//        int entityID = NettySession.getEntityIDFromChannel(ctx.channel());
//
//        if (IdleStateEvent.class.isAssignableFrom(evt.getClass()) && entityID > 0) {
//            IdleStateEvent event = (IdleStateEvent) evt;
//            if (event.state() == IdleState.READER_IDLE) {
//                IDeviceConfigService cofigSPIService = SPIServiceFactory.findService(IDeviceConfigService.class,clientType);
//                logger.info("超时离线事件,clienttype->{}, entityID->{}", clientType, entityID);
//                Message msg = new Message();
//                msg.setClientType(clientType);
//                msg.setEntityID(entityID);
//                msg.setLevelcode(NettySession.getLevelCodeFromChannel(ctx.channel()));
//                service.onLogOut(msg);
//                ctx.disconnect();
//            }
//        }
//    }
//
//    private void handleWatchedMsg(final Channel channel, final Message msg) {
//        byte[] msgFunction = msg.getFunction();
//        for (TV tv : msg.getTvList()) {
//            List<WatchedMsg> watchedMsgList = NettySession.getWatchedMsgFromChannel(channel, tv.getTag(), msgFunction);
//            if (null != watchedMsgList && !watchedMsgList.isEmpty()) {
//                for (WatchedMsg watchedMsg : watchedMsgList) {
//                    //这里不能直接使用msg的set. 应该深度克隆一个对象再发送.
//                    msg.setWatchedMsg(watchedMsg);
//                    Message newMsg = msg.clone();
//                    //发送到消息队列.
//                    boolean sendSuc = MsgProducer.batchProduce(MsgT.blackTea_general_msg_watched, new MsgB(msg.getLevelcode(), msg.getEntityID(), null), newMsg, watchedMsg.getSenderServiceID());
//                }
//                NettySession.deleteWatchedMsg2Session(channel, tv.getTag(), msgFunction);
//            }
//        }
//    }
//
//    /**
//     * 这里是单线程的, 如果调用多线程, 会有消息的时序问题.,最好是批量消息,+时间窗结合,来通信.
//     *
//     * @throws Exception
//     */
//    @Override
//    protected void channelRead0(final ChannelHandlerContext ctx, final Message msg) throws Exception {
//        Channel channel = ctx.channel();
//        //处理session相关信息
//        recordSession(ctx, msg);
//        int port = NettySession.getRequestPortFromChannel(ctx.channel());
//        int client = ClientTypeEnum.getEnumByPort(port).getClientType();
//        service = SPIServiceFactory.findService(IDeviceConfigService.class,client);
//        //处理监听的消息,返回服务端的控制或者查询结果
//        handleWatchedMsg(channel, msg);
//        if (service.isAddressMsg(msg)) {
//            logger.info("收到寻址消:{}  mag:{}", NettySession.getCurrentTextMsgFromChannel(channel), msg);
//            msg.setClientType(client);
//            Message respMsg = service.handleAddress(msg);
//            if (null != respMsg) {
//                ctx.channel().writeAndFlush(respMsg);
//            } else {
//                //强制关闭
//                channel.close();
//                logger.info("寻址消息异常,源消息:{}  mag:{}", NettySession.getCurrentTextMsgFromChannel(channel), msg);
//            }
//            NettySession.removeCurrentTextMsgFromChannel(channel);
//            return;
//        }
//        if (service.isLoginMsg(msg)) {
//            //发起登录
//            Message loginResult = service.login(msg);
//            if (null == loginResult) {
//                logger.info("登录失败,断开连接.{}", msg.toString());
//                closeChannel(channel);
//                return;
//            } else {
//                logger.info("登录成功：{}", loginResult.toString());
//                //首先增加一个当前接入端的数量
////                ClientTypeEnum clientEnum = ClientTypeEnum.getEnumByPort(port);
////                if (clientEnum!=null) {
////                    new AcceptorStatusHandler(clientEnum).addConn();
////                }
//                AcceptorConfig.instance(port).getAcceptorStatusHandler().addConn();
//                int clientType = loginResult.getClientType();
//                int entityID = loginResult.getEntityID();
//                String levelcode = loginResult.getLevelcode();
//
//                //需要处理设备的session信息
//                String imei;
//                if (ClientTypeEnum.GL500.getClientType() == clientType || ClientTypeEnum.BSJ.getClientType() == clientType) {
//                    //除开otu设备的其他所有设备，需要从登陆的返回的loginResult对象中获取IMEI
//                    imei = getValue("101", loginResult);
//                } else {
//                    //otu设备
//                    imei = getValue("101", msg);
//                }
//                NettySession.recordSession(channel, clientType, entityID, levelcode, imei);
//                msg.setClientType(clientType);
//                msg.setEntityID(entityID);
//                msg.setLevelcode(levelcode);
//
//                //每次登陆需要冲洗记录server的uuid, 下行消息是通过MQ找到server的UUID来推送的, 每次必须不一样
//                KV_Client kv_Client = new KV_Client();
//                kv_Client.setServiceID(ConsumerConfig.instance().getServiceID()).setLoginTime(System.currentTimeMillis()).setLastMsgTime(System.currentTimeMillis()).saveOrUpdate(clientType, entityID);
//                //再修改当前msg的信息
//                msg.setClientType(loginResult.getClientType());
//                msg.setEntityID(loginResult.getEntityID());
//
//                //记录id和channel的对应关系
//                Channel currentChannel = NettySession.getChannelFromSession(loginResult.getEntityID());
//                if (null != currentChannel) {
//                    //踢掉之前的连接
//                    try {
//                        currentChannel.close();
//                        //标记为被替换
//                        NettySession.recordIsReplacedChannel(currentChannel, true);
//                        //清除.
//                        NettySession.removeChannelFromSession(loginResult.getEntityID());
//                    } catch (Exception e) {
//                        logger.error(e.getMessage(), e);
//                    }
//                }
//
//                NettySession.setChannelToSession(loginResult.getEntityID(), channel);
//
//                service.onLogIn(msg);
//
//                //记录登录的日志
//                String currentTextMsg = NettySession.getCurrentTextMsgFromChannel(channel);
//                if (StrUtil.isNotBlank(currentTextMsg)) {
//                    ClientLog log = new ClientLog(clientType, entityID, imei, EventConstant.MSG_UP, currentTextMsg);
//                    log.setServer(AddressUtil.getLanIPCache());
//                    terminalLogger.info(com.alibaba.fastjson.JSON.toJSONString(log));
//                    //再删除
//                    NettySession.removeCurrentTextMsgFromChannel(channel);
//                }
//            }
//        }
//
//        //其他所有消息都要调用设备服务的更新kv_client对象和数据库tb_terminal表中的last_msg_time
//        service.updateRDBLastMsgTime(NettySession.getClientTypeFromChannel(channel), NettySession.getEntityIDFromChannel(channel));
//        //往消息队列里面扔
//        //有可能是心跳消息
//        if (null != msg.getFunction()) {
//            logger.info("生产消息：{}", msg.toString());
////            logger.info("send2MQ,略");
//            send2MQ(msg,ClientTypeEnum.getEnumByPort(port));
//        }
//        //响应消息
//        response(ctx, msg);
//    }
//
//    /**
//     * 如果某些消息需要立即响应.直接在接入端回复响应消息
//     *
//     * @param ctx
//     * @param msg
//     */
//    private void response(final ChannelHandlerContext ctx, final Message msg) {
//        Message respMsg = service.getResponseMsg(msg);
//        if (null != respMsg) {
//            ctx.channel().writeAndFlush(respMsg);
//        }
//    }
//
//
//
//    /**
//     * 处理session信息, 保存ttl
//     *
//     * @param ctx
//     * @param msg
//     */
//    private void recordSession(final ChannelHandlerContext ctx, final Message msg) {
//        try {
//            Channel channel = ctx.channel();
//            int port = NettySession.getRequestPortFromChannel(channel);
//            boolean RDBTTLConfiged = AcceptorConfig.instance(port).getRDBTTLConfigValue() > 0;
//            //尚未初始化,第一次登录
//            if (null == NettyContext.get(channel, CommunicationConfig.NETTY_SESSIONKEY_TTL)) {
//                NettyContext.put(channel, CommunicationConfig.NETTY_SESSIONKEY_TTL, System.currentTimeMillis());
//                //开启了更新关系型数据库
//                if (RDBTTLConfiged) {
//                    NettyContext.put(channel, CommunicationConfig.NETTY_SESSIONKEY_RDBTTL, System.currentTimeMillis());
//                }
//            }
//            //赋值
//            msg.setClientType(NettySession.getClientTypeFromChannel(channel));
//            msg.setEntityID(NettySession.getEntityIDFromChannel(channel));
//            msg.setLevelcode(NettySession.getLevelCodeFromChannel(channel));
//
//            long ttl = System.currentTimeMillis() - (long) NettyContext.get(channel, CommunicationConfig.NETTY_SESSIONKEY_TTL);
//            //只要超过一半时间,就必须更新一次
//            if (ttl >= AcceptorConfig.instance(port).getTTLConfigValue() / 2) {
//                logger.info("更新心跳:clienttype:{},entityid:{}", msg.getClientType(), msg.getEntityID());
//                //已经登录成功
//                if (msg.getEntityID() > 0 && msg.getClientType() > 0) {
//                    //更新ttl
//                    NettyContext.put(channel, CommunicationConfig.NETTY_SESSIONKEY_TTL, System.currentTimeMillis());
//                    //记录到数据库
//                    if (msg.getClientType() != ClientTypeEnum.OTU.getClientType()) {
//                        //由于otu设备需要在terminal服务中处理lastmsg，所有这里需要过滤
//                        KV_Client.dao.setLastMsgTime(System.currentTimeMillis()).saveOrUpdate(msg.getClientType(), msg.getEntityID());
//                    }
//                    //刷新id对应关系
//                    NettySession.setChannelToSession(msg.getEntityID(), channel);
//                    //开启了更新关系型数据库
//                    if (RDBTTLConfiged) {
//                        NettyContext.put(channel, CommunicationConfig.NETTY_SESSIONKEY_RDBTTL, System.currentTimeMillis());
//                        //记录到数据库--由于business-otu-sirui迁移，则otu设备不需要进行处理
//                        if (!(msg.getClientType() == ClientTypeEnum.OTU.getClientType() || msg.getClientType() == ClientTypeEnum.BSJ.getClientType())) {
////                            IDeviceLoginService_OTU service = RPC.callSync(IDeviceLoginService_OTU.class);
////                            service.updateRDBLastMsgTime(msg);
//                        }
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }
//    }
//
//    /**
//     * 发送到消息队列
//     *
//     * @param msg
//     */
//    private void send2MQ(Message msg,ClientTypeEnum client) {
//        boolean suc = MsgProducer.batchProduce(client.getMsgUpTopic(), new MsgB(msg.getLevelcode(), msg.getEntityID(), null), msg);
//        if (!suc) {
//            logger.error("发送到消息队列出错,{}", msg);
//        }
//    }
//
//    /**
//     * 登录失败 需要断开
//     */
//    private void closeChannel(final Channel channel) {
//        NettyContext.removeAll(channel);
//        channel.close();
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        int clientType = NettySession.getClientTypeFromChannel(ctx.channel());
//        int entityID = NettySession.getEntityIDFromChannel(ctx.channel());
//        logger.warn("Unexpected exception from downstream.entity:" + entityID, cause);
//        if (entityID > 0) {
//            IDeviceConfigService service = SPIServiceFactory.findService(IDeviceConfigService.class);
//            logger.info("超时离线事件,clienttype->{}, entityID->{}", clientType, entityID);
//            Message msg = new Message();
//            msg.setClientType(clientType);
//            msg.setEntityID(entityID);
//            msg.setLevelcode(NettySession.getLevelCodeFromChannel(ctx.channel()));
//            //被踢下去的处理是不同的
//            Map<String, Boolean> map = Kv.by(CommunicationConfig.NETTY_SESSIONKEY_isReplaced, NettySession.getIsRemovedFromChannel(ctx.channel()));
//            msg.setExtend(Lists.newArrayList(SerializeUtil.toByte(map)));
//            service.onLogOut(msg);
//        }
//
//        closeChannel(ctx.channel());
//        ctx.close();
//    }
//}
