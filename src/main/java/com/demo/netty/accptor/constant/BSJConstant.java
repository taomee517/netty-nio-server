/**
 *
 */
package com.demo.netty.accptor.constant;

/**
 * @author luosanjun
 */
public class BSJConstant {
    public static int GSP_LENGTH = 6 + 1 + 4 + 4 + 1 + 2;
    public static int LBS_LENGTH = 1 + 2 + 1 + 2 + 3;
    public static final int MIN_LENTH = 2 + 1 + 1 + 2 + 2 + 2;

    public static byte[] START_POSITION = {0x78, 0x78};
    public static byte[] STOP_POSITION = {0x0D, 0x0A};

    //记录虚拟心跳的时间的key
    public static final String VIRTUAL_HEART_KEY = "vhk";

}
