///**
// *
// */
//package com.demo.netty.accptor.util;
//
//import com.blackTea.common.model.Message;
//import com.blackTea.util.log.LogUtil;
//
///**
// * @author yu.hou, email: houyujiangjun@qq.com
// * @date 2017年11月30日
// */
//public class GL500CodecUtil {
//    private static org.slf4j.Logger logger = LogUtil.getLogger(GL500CodecUtil.class);
//
//    /**
//     * 是否是心跳消息
//     *
//     * @param msg
//     * @return
//     */
//    public static boolean isHeartMsg(Message msg) {
//        return null == msg || null == msg.getFunction();
//    }
//
//    /**
//     * 将文本转化为nettyMsg
//     *
//     * @param in
//     * @return
//     * @throws Exception
//     */
//    public static void deSerialize(String in) throws Exception {
//    }
//
//    /**
//     * 序列化，把对象转成字符串
//     */
//    public static String serialize(Message outMsg) throws Exception {
//        return null;
//    }
//
//}
