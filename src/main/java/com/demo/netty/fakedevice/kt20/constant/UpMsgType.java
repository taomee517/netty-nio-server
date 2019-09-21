package com.demo.netty.fakedevice.kt20.constant;

import com.fzk.sdk.constant.MessageEnum;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/4/18
 * @time 10:03
 */
@Getter
public enum UpMsgType {
    /**应答消息*/
    ACK(0X0001,"应答消息", MessageEnum.ACK),

    /**登录消息*/
    HEART_BEAT(0x0002, "心跳消息", MessageEnum.HEART_BEAT),

    /**注册消息*/
    REGISTER(0x0100, "注册消息", MessageEnum.AS),

    /**终端注销*/
    LOGOUT(0x0003,"终端注销", null),

    /**鉴权消息*/
    AUTH(0x0102, "鉴权消息", MessageEnum.CONFIG),

    /**查询结果*/
    QUERY_RESULT(0x0104, "查询结果", MessageEnum.CONFIG),

    /**位置信息*/
    LOCATION(0x0200, "位置信息", MessageEnum.STATUS),

    /**位置信息查询应答*/
    LOCATION_QUERY_DATA(0x0201, "位置信息查询应答", MessageEnum.STATUS),

    /**批量定位数据*/
    MULTI_LOCATION(0X0704, "批量定位数据", MessageEnum.STATUS),

    /**文本消息*/
    TEXT_MSG(0X6006, "文本消息", MessageEnum.RESULT),

    /**透传上行消息*/
    PASSTHROUGH(0x0900,"透传消息", MessageEnum.UNKNOWN),


    ;

    private Integer msgId;
    private String desc;
    private MessageEnum messageType;
    public static Map<Integer, MessageEnum> TYPE_MAP = new HashMap<>();
    public static Map<Integer, UpMsgType> PROTOCOL_MAP = new HashMap<>();

    UpMsgType(Integer msgId, String desc, MessageEnum messageType){
        this.messageType = messageType;
        this.desc = desc;
        this.msgId = msgId;
    }

    static {
        UpMsgType[] enums = values();
        for(UpMsgType procotol : enums){
            TYPE_MAP.put(procotol.msgId,procotol.messageType);
            PROTOCOL_MAP.put(procotol.msgId,procotol);
        }
    }

    public static UpMsgType getProtocolByMsgId(Integer msgId){
        return PROTOCOL_MAP.get(msgId);
    }

    public static MessageEnum getMessageTypeByNo(Integer msgId){
        return TYPE_MAP.get(msgId);
    }



}
