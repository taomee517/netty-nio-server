/**
 *
 */
package com.demo.netty.accptor.constant;

/**
 * @author jgchen
 */
public class GL500Constants {

    //放到Message的function中的参数名，用于标记gl500设备的消息是登录消息
    public static final String LOGIN_FUNCTION = "LOGIN";

    //放到Message的function中的参数名，用于标记gl500设备的消息是登录消息
    public static final String HEART_FUNCTION = "HEART";

    //放到busi那边处理的上线任务
    public static final String LOGIN_TASK_FUNCTION = "LOGIN_TASK";

    public static final String ACK_PREFIX = "+ACK";

    public static final byte START_SIGN = '+';

    //设备主动上报的开头
    public static final String RESP_PREFIX = "+RESP:";


    //结尾
    public static final byte END_SIGN = '$';

    //指令标签的开头
    public static final String TAG_PREFIX = "GT";

    //记录虚拟心跳的时间的key
    public static final String VIRTUAL_HEART_KEY = "vhk";

    //    //下发内容的格式的模板
    public static final String AT_FORMAT = "AT+GT{0}={1}$";

    public static final String AT = "AT";

    public static final String UNKOWN_SOFTVERSION = "unknown";

    public static final long VIRTUAL_HEART_KEY_INTERVAL = 2 * 60 * 1000;

}
