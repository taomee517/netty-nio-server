///**
// *
// */
//package com.demo.netty.accptor.constant;
//
//import mq.m.MsgC;
//import mq.m.MsgT;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * blackTea平台可以接入的设备
// *
// * @author yu.hou, email: houyujiangjun@qq.com
// * @date 2017年11月28日
// */
//public enum ClientTypeEnum {
//    /**
//     * OTU设备, 端口2103,TTL五分钟,blackTea_otu_msg_up,blackTea_otu_msg_down,bleakTea_otu_msg_down
//     */
//    OTU(3, 2103, 5 * 60 * 1000L, MsgT.blackTea_otu_msg_up, MsgT.blackTea_otu_msg_down, MsgC.bleakTea_otu_msg_down),
//    /**
//     * kelong设备
//     */
//    KeLong(22, 2110, 5 * 60 * 1000L, MsgT.blackTea_kelong_msg_up, MsgT.blackTea_kelong_msg_down, MsgC.bleakTea_kelong_msg_down),
//
//    /**
//     * 博实结设备
//     */
//    BSJ(23, 2111, 5 * 60 * 1000L, MsgT.blackTea_bsj_msg_up, MsgT.blackTea_bsj_msg_down, MsgC.bleakTea_bsj_msg_down),
//
//    /**
//     * GL500无线追踪设备
//     */
//    GL500(17, 2107, 5 * 60 * 1000L, MsgT.blackTea_gl500_msg_up, MsgT.blackTea_gl500_msg_down, MsgC.bleakTea_gl500_msg_down),
//
//    /**
//     * 蓝牙设备
//     */
//    BT(16, 0, 0, null, null, null);
//
//    private int clientType;
//    private long ttl;//time to live
//    private int port;
//    private MsgT msgDownTopic;
//    private MsgT msgUpTopic;
//    private MsgC msgDownConsumer;
//
//    /**
//     * @param clientType
//     * @param port
//     * @param ttl
//     * @param msgUpTopic
//     * @param msgDownTopic
//     * @param msgDownConsumer
//     */
//    private ClientTypeEnum(int clientType, int port, long ttl, MsgT msgUpTopic, MsgT msgDownTopic, MsgC msgDownConsumer) {
//        this.clientType = clientType;
//        this.ttl = ttl;
//        this.port = port;
//        this.msgUpTopic = msgUpTopic;
//        this.msgDownTopic = msgDownTopic;
//        this.msgDownConsumer = msgDownConsumer;
//    }
//
//    /**
//     * @return
//     */
//    public MsgC getMsgDownConsumer() {
//        return msgDownConsumer;
//    }
//
//    /**
//     * @return
//     */
//    public MsgT getMsgUpTopic() {
//        return msgUpTopic;
//    }
//
//    /**
//     * @return
//     */
//    public MsgT getMsgDownTopic() {
//        return msgDownTopic;
//    }
//
//    public int getClientType() {
//        return clientType;
//    }
//
//    public int getPort() {
//        return port;
//    }
//
//    /**
//     * 单位 毫秒
//     *
//     * @return
//     */
//    public long getTTL() {
//        return ttl;
//    }
//
//    public final static Map<Integer, ClientTypeEnum> valueMap = new HashMap<>();
//    public final static Map<String, ClientTypeEnum> nameMap = new HashMap<>();
//    public final static Map<Integer, ClientTypeEnum> portMap = new HashMap<>();
//
//    static {
//        for (ClientTypeEnum t : values()) {
//            valueMap.put(t.getClientType(), t);
//            nameMap.put(t.name(), t);
//            portMap.put(t.getPort(),t);
//        }
//    }
//
//    public static ClientTypeEnum getEnumByName(String name) {
//        ClientTypeEnum me = nameMap.get(name);
//        if (null != me) {
//            return me;
//        } else {
//            return null;
//        }
//    }
//
//    /**
//     * 根据值获取实例
//     *
//     * @param clientType
//     * @return 可能会返回null
//     */
//    public static ClientTypeEnum getEnumByClientType(int clientType) {
//        ClientTypeEnum me = valueMap.get(clientType);
//        if (null != me) {
//            return me;
//        } else {
//            return null;
//        }
//    }
//
//    /**
//     * 根据端口获取枚举类
//     *
//     * @param port
//     * @return 可能会返回null
//     */
//    public static ClientTypeEnum getEnumByPort(int port) {
//        ClientTypeEnum me = portMap.get(port);
//        if (null != me) {
//            return me;
//        } else {
//            return null;
//        }
//    }
//}
