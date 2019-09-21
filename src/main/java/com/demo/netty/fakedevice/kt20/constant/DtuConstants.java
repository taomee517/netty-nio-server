package com.demo.netty.fakedevice.kt20.constant;

public class DtuConstants {

    /**开始、结束符*/
    public static final byte SIGN_CODE = 0x7e;
    public static final String CHARSET = "GB2312";

    /**转义字符相关*/
    public static final byte ESCAPE_CHAR_E_SRC = 0x7e;
    public static final byte ESCAPE_CHAR_D_SRC = 0x7d;
    public static final byte[] ESCAPE_CHAR_D_TRANS = {0x7d,0x01};
    public static final byte[] ESCAPE_CHAR_E_TRANS = {0x7d,0x02};

    /**经，纬度还原倍数*/
    public static final double COORDINATE_BASE = 0.000001;

    /**数据包最小长度:起始位(1) + 消息id(2) + 消息体属性(2) + 终端手机号(6) + 消息流水号（2） + 校验码(1) + 停止位(1)*/
    public static final int MIN_LENGTH = 1 + 2 + 2 + 6 + 2 + 1 + 1;

    /**消息id下标及长度*/
    public static final int MESSAGE_TYPE_INDEX = 1;
    public static final int MESSAGE_TYPE_LENGTH = 2;

    /**消息体属性下标及长度*/
    public static final int CONTENT_PROPERTIES_INDEX = 3;
    public static final int CONTENT_PROPERTIES_LENGTH = 2;

    /**终端手机号下标及长度*/
    public static final int SN_INDEX = 5;
    public static final int SN_LENGTH = 6;

    /**序列号下下标 及长度*/
    public static final int SERIAL_NO_INDEX = 11;
    public static final int SERIAL_NO_LENGTH = 2;

    /**透传协议相关*/
    //fzk透传消息采用串口1透传，透传类型为0x41
    public static final byte TRANSMIT_TYPE = 0x41;
    public static final String PARAM_SPLIT_SIGN = ",";
    public static final String LONG_PARAM_SPLIT_SIGN = "\\.";
    public static final String TRANSMIT_CONCAT_SIGN = "#";
    public static final String TRANSMIT_HANDLER_PACKAGE ="com.fzk.dtu.protocol.localbus.handler";
    public  static final String TRANSMIT_TIME_QUERY_TAG = "b709";
    public static final String DEFAULT_AUTH_CODE = "FZK-BSJ-KT20-CONFIRM";

}
