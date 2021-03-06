package com.demo.netty.basic.constant;

import java.nio.charset.Charset;

public class DefaultValue {
    public static final String DEFAULT_FIRST_MSG = "Hello,World!";
    public static final String NOT_FOUND = "404";
    public static final String DEFAULT_TEST_IP = "127.0.0.1";
    public static final int DEFAULT_TEST_PORT = 8001;
    /**默认字符集*/
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    /**默认Netty Boss/Worker线程数*/
    public static final int DEFAULT_BOSS_THREAD = 1;
    public static final int DEFAULT_WORKER_THREAD = 16;
    /**单条消息最大长度限制*/
    public static final int MSG_MAX_SIZE = 512;
}
