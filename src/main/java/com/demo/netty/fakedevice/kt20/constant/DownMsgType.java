//package com.demo.netty.fakedevice.kt20.constant;
//
//import com.fzk.sdk.constant.MessageEnum;
//import lombok.Getter;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author LuoTao
// * @email taomee517@qq.com
// * @date 2019/4/18
// * @time 10:03
// */
//@Getter
//public enum DownMsgType {
//    /**平台通用应答*/
//    RESPONSE(0x8001,"平台通用应答"),
//
//    /**终端注册应答*/
//    REGISTER_ACK(0x8100, "终端注册应答"),
//
//    /**数据下行透传*/
//    TRANSMIT(0x8900, "数据下行透传"),
//
//    /**设置终端参数*/
//    SETTING(0x8103,"设置终端参数"),
//
//    /**查询终端参数*/
//    QUERY(0x8104, "查询终端参数"),
//
//    /**位置信息查询*/
//    LOCATION(0x8201, "位置信息查询"),
//
//    /**文本信息下发*/
//    TEXT(0x8300, "文本信息下发"),
//
//    ;
//
//    private Integer msgId;
//    private String desc;
//    public static Map<Integer, MessageEnum> TYPE_MAP = new HashMap<>();
//    public static Map<Integer, DownMsgType> PROTOCOL_MAP = new HashMap<>();
//
//    DownMsgType(Integer msgId, String desc){
//        this.desc = desc;
//        this.msgId = msgId;
//    }
//
//    static {
//        DownMsgType[] enums = values();
//        for(DownMsgType procotol : enums){
//            PROTOCOL_MAP.put(procotol.msgId,procotol);
//        }
//    }
//
//    public static DownMsgType getProtocolByMsgId(Integer msgId){
//        return PROTOCOL_MAP.get(msgId);
//    }
//
//    public static MessageEnum getMessageTypeByNo(Integer msgId){
//        return TYPE_MAP.get(msgId);
//    }
//
//
//
//}
