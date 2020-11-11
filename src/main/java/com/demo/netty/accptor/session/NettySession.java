///**
// *
// */
//package com.demo.netty.accptor.session;
//
//import com.blackTea.common.cache.CacheUtil;
//import com.blackTea.common.communicate.netty.config.CommunicationConfig;
//import com.blackTea.common.communicate.netty.model.NettyContext;
//import com.blackTea.common.model.WatchedMsg;
//import com.blackTea.util.BytesUtil;
//import com.blackTea.util.log.LogUtil;
//import io.netty.channel.Channel;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author yu.hou, email: houyujiangjun@qq.com
// * @date 2017年12月4日
// */
//public class NettySession {
//
//    public final static String IMEI = "imei";
//
//    private static org.slf4j.Logger logger = LogUtil.getLogger(NettySession.class);
//
//    public final static String REQUEST_PORT = "port";
//
//    /**
//     * 从session中获取channel, 过期时间十分钟
//     *
//     * @param entityID
//     * @return
//     */
//    public static Channel getChannelFromSession(int entityID) {
//        return CacheUtil.nettySession_id2channel.getIfPresent(entityID);
//    }
//
//    /**
//     * 设置到session中
//     *
//     * @param entityID
//     * @param channel
//     */
//    public static void setChannelToSession(int entityID, Channel channel) {
//        CacheUtil.nettySession_id2channel.put(entityID, channel);
//    }
//
//    /**
//     * 移除
//     *
//     * @param entityID
//     */
//    public static void removeChannelFromSession(int entityID) {
//        CacheUtil.nettySession_id2channel.invalidate(entityID);
//    }
//
//    /**
//     * 记录基本信息到netty的session中
//     *
//     * @param channel
//     * @param clienttype
//     * @param entityID
//     * @param levelcode
//     */
//    public static void recordSession(Channel channel, int clienttype, int entityID, String levelcode, String imei) {
//        NettyContext.put(channel, CommunicationConfig.NETTY_SESSIONKEY_CLIENTTYPE, clienttype);
//        NettyContext.put(channel, CommunicationConfig.NETTY_SESSIONKEY_ENTITYID, entityID);
//        NettyContext.put(channel, CommunicationConfig.NETTY_SESSIONKEY_LEVELCODE, levelcode);
//        NettyContext.put(channel, IMEI, imei);
//    }
//
//    public static void recordWatchedMsg2Session(Channel channel, WatchedMsg watchedMsg) {
//        try {
//            synchronized (channel) {
//                List<WatchedMsg> list = new ArrayList<>();
//                Object obj = get(channel, CommunicationConfig.NETTY_SESSIONKEY_WATCHEDMSG);
//                if (null != obj) {
//                    list = (List<WatchedMsg>) obj;
//                }
//                boolean isIncluded = false;
//                for (WatchedMsg m : list) {
//                    if (BytesUtil.equals(m.getWatchedTag(), watchedMsg.getWatchedTag()) && m.getSenderServiceID() == watchedMsg.getSenderServiceID()
//                            && BytesUtil.equals(m.getWatchedFunction(), watchedMsg.getWatchedFunction())) {
//                        isIncluded = true;
//                        break;
//                    }
//                }
//                if (!isIncluded) {//不包含
//                    list.add(watchedMsg);
//                    NettyContext.put(channel, CommunicationConfig.NETTY_SESSIONKEY_WATCHEDMSG, list);
//                }
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }
//    }
//
//    /**
//     * 有成功的消息之后,应该直接删除对应的元素
//     *
//     * @param channel
//     * @param msgTag
//     * @param msgFunction
//     */
//    public static void deleteWatchedMsg2Session(Channel channel, byte[] msgTag, byte[] msgFunction) {
//        try {
//            synchronized (channel) {
//                List<WatchedMsg> list = new ArrayList<>();
//                Object obj = get(channel, CommunicationConfig.NETTY_SESSIONKEY_WATCHEDMSG);
//                if (null != obj) {
//                    list = (List<WatchedMsg>) obj;
//                }
//                List<WatchedMsg> result = new ArrayList<>();
//                for (WatchedMsg m : list) {
//                    if (BytesUtil.equals(m.getWatchedTag(), msgTag) && BytesUtil.equals(m.getWatchedFunction(), msgFunction)) {
//                    } else {//非命中目标
//                        result.add(m);
//                    }
//                }
//                NettyContext.put(channel, CommunicationConfig.NETTY_SESSIONKEY_WATCHEDMSG, result);
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }
//    }
//
//    private static Object get(Channel channel, String key) {
//        return NettyContext.get(channel, key);
//    }
//
//    /**
//     * 是否有对应的监控消息
//     *
//     * @param channel
//     * @param msgTag
//     * @param msgFunction
//     * @return
//     */
//    public static List<WatchedMsg> getWatchedMsgFromChannel(Channel channel, byte[] msgTag, byte[] msgFunction) {
//        List<WatchedMsg> list = new ArrayList<>();
//        Object obj = get(channel, CommunicationConfig.NETTY_SESSIONKEY_WATCHEDMSG);
//        if (null != obj) {
//            list = (List<WatchedMsg>) obj;
//        }
//        List<WatchedMsg> result = new ArrayList<>();
//        for (WatchedMsg m : list) {
//            if (BytesUtil.equals(m.getWatchedTag(), msgTag) && BytesUtil.equals(m.getWatchedFunction(), msgFunction)) {
//                result.add(m);
//            }
//        }
//        return result;
//    }
//
//    public static int getClientTypeFromChannel(Channel channel) {
//        Object obj = get(channel, CommunicationConfig.NETTY_SESSIONKEY_CLIENTTYPE);
//        if (null != obj) {
//            return (int) obj;
//        } else {
//            return 0;
//        }
//    }
//
//    /**
//     * 是否已经登录
//     *
//     * @param channel
//     * @return
//     */
//    public static boolean isLoginIn(Channel channel) {
//        return getClientTypeFromChannel(channel) != 0 && getEntityIDFromChannel(channel) != 0;
//    }
//
//    public static int getEntityIDFromChannel(Channel channel) {
//        Object obj = get(channel, CommunicationConfig.NETTY_SESSIONKEY_ENTITYID);
//        if (null != obj) {
//            return (int) obj;
//        } else {
//            return 0;
//        }
//    }
//
//    public static String getImeiFromChannel(Channel channel){
//        Object obj = get(channel,IMEI);
//        if (null != obj) {
//            return (String) obj;
//        } else {
//            return null;
//        }
//    }
//
//
//    public static String getCurrentTextMsgFromChannel(Channel channel) {
//        Object obj = get(channel, CommunicationConfig.NETTY_SESSIONKEY_currentTextMsg);
//        if (null != obj) {
//            return (String) obj;
//        } else {
//            return null;
//        }
//    }
//
//    /**
//     * 记录登录之前的消息到channel中
//     *
//     * @param channel
//     * @param currentTextMsg
//     */
//    public static void recordCurrentTextMsg2Channel(Channel channel, String currentTextMsg) {
//        NettyContext.put(channel, CommunicationConfig.NETTY_SESSIONKEY_currentTextMsg, currentTextMsg);
//    }
//
//    public static void removeCurrentTextMsgFromChannel(Channel channel) {
//        NettyContext.remove(channel, CommunicationConfig.NETTY_SESSIONKEY_currentTextMsg);
//    }
//
//    public static boolean getIsRemovedFromChannel(Channel channel) {
//        Object obj = get(channel, CommunicationConfig.NETTY_SESSIONKEY_isReplaced);
//        if (null != obj) {
//            return (boolean) obj;
//        } else {
//            return false;
//        }
//    }
//
//    public static void recordIsReplacedChannel(Channel channel, boolean isReplaced) {
//        NettyContext.put(channel, CommunicationConfig.NETTY_SESSIONKEY_isReplaced, isReplaced);
//    }
//
//    public static void removeIsReplacedFromChannel(Channel channel) {
//        NettyContext.remove(channel, CommunicationConfig.NETTY_SESSIONKEY_isReplaced);
//    }
//
//    public static void recordEncryptMsgFromChannel(Channel channel, boolean isEncryptMsg) {
//        NettyContext.put(channel, CommunicationConfig.NETTY_SESSIONKEY_isEncryptMsg, isEncryptMsg);
//    }
//
//    public static boolean getIsEncryptMsgFromChannel(Channel channel) {
//        Object obj = get(channel, CommunicationConfig.NETTY_SESSIONKEY_isEncryptMsg);
//        if (null != obj) {
//            return (boolean) obj;
//        } else {
//            return true;//注意, 默认是加密的
//        }
//    }
//
//    public static String getLevelCodeFromChannel(Channel channel) {
//        Object obj = get(channel, CommunicationConfig.NETTY_SESSIONKEY_LEVELCODE);
//        if (null != obj) {
//            return (String) obj;
//        } else {
//            return null;
//        }
//    }
//
//    /**记录请求端口*/
//    public static void recordRequestPort2Channel(Channel channel,int port) {
//        NettyContext.put(channel, REQUEST_PORT, port);
//    }
//
//    /**获取请求端口*/
//    public static int getRequestPortFromChannel(Channel channel) {
//        Object obj = get(channel,REQUEST_PORT);
//        if (null != obj) {
//            return (int) obj;
//        } else {
//            return 0;
//        }
//    }
//}
