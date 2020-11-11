///**
// *
// */
//package com.demo.netty.accptor.config;
//
//import com.blackTea.common.acceptor.AcceptorStatusHandler;
//import com.blackTea.common.constants.ClientTypeEnum;
//import com.blackTea.common.distribute.config.PathEnum;
//import com.blackTea.common.spi.SPIServiceFactory;
//import com.blackTea.util.StrUtil;
//import com.demo.netty.accptor.service.IDeviceConfigService;
//import lombok.extern.slf4j.Slf4j;
//import mq.m.MsgC;
//import mq.m.MsgT;
//
///**
// * 由于ttl信息只需加载一次, 为了省掉spi的开销, 决定用单例来承载数据
// *
// * @author yu.hou, email: houyujiangjun@qq.com
// * @date 2017年12月4日
// */
//
//@Slf4j
//public class AcceptorConfig {
//    /**
//     * 六秒不心跳,就算过期
//     */
//    public static final long SERVICE_TIMEOUT_MILLIS = 6000L;
//    private MsgT msgTopic_up = null;
//    private MsgT msgTopic_down = null;
//    private MsgC msgConsumer_down = null;
//    private long ttl = 0;
//    private long RDBttl = 0;
//    private ClientTypeEnum clientTypeEnum;
//    private AcceptorStatusHandler acceptorStatusHandler;
//
//    /**
//     * @return
//     */
//    public MsgT getMsgTopic_up() {
//        return msgTopic_up;
//    }
//
//    /**
//     * @return
//     */
//    public MsgT getMsgTopic_down() {
//        return msgTopic_down;
//    }
//
////    private static class SingletonHolder {
////        public final static AcceptorConfig instance = new AcceptorConfig();
////    }
//
//    public static AcceptorConfig instance(int port) {
//        return  new AcceptorConfig(port);
//    }
//
//    public AcceptorConfig(int port) {
//        IDeviceConfigService service = null;
//        try {
//            int clientType = ClientTypeEnum.getEnumByPort(port).getClientType();
//            service = SPIServiceFactory.findService(IDeviceConfigService.class,clientType);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            System.err.println("ttl信息加载错误,系统退出");
//            System.exit(0);
//            return;
//        }
//        ttl = service.getClientTypeEnum().getTTL();
//        if (ttl == 0) {
//            log.error("ttl信息为0信息错误,系统退出");
//            System.err.println("ttl信息为0信息错误,系统退出");
//            System.exit(0);
//        }
//        RDBttl = service.getTTL4RDBLastMsgTime();
//        msgTopic_up = service.getClientTypeEnum().getMsgUpTopic();
//        msgTopic_down = service.getClientTypeEnum().getMsgDownTopic();
//        msgConsumer_down = service.getClientTypeEnum().getMsgDownConsumer();
//        clientTypeEnum = service.getClientTypeEnum();
//        acceptorStatusHandler = new AcceptorStatusHandler(clientTypeEnum);
//    }
//
//    /**
//     * @return
//     */
//    public AcceptorStatusHandler getAcceptorStatusHandler() {
//        return acceptorStatusHandler;
//    }
//
//    /**
//     * @return
//     */
//    public ClientTypeEnum getClientTypeEnum() {
//        return clientTypeEnum;
//    }
//
//    /**
//     * @return
//     */
//    public MsgC getMsgConsumer_down() {
//        return msgConsumer_down;
//    }
//
//    /**
//     * 服务发现时的acceptor path
//     *
//     * @param clientType
//     * @return
//     */
//    public static String getAcceptorServicePath(ClientTypeEnum clientType) {
//        return StrUtil.joint(PathEnum.service_prefix.getPathValue(), "acceptor", clientType);
//    }
//
//    /**
//     * 通过spi获取不同设备配置的ttl值.
//     *
//     * @return
//     */
//    public long getTTLConfigValue() {
//        return ttl;
//    }
//
//    /**
//     * 更新到关系型数据库的频率
//     *
//     * @return
//     */
//    public long getRDBTTLConfigValue() {
//        return RDBttl;
//    }
//
//}
