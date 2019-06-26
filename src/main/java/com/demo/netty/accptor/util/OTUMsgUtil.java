/**
 *
 */
package com.demo.netty.accptor.util;

import com.blackTea.common.model.Message;
import com.blackTea.common.model.TV;

/**
 * @author yu.hou, email: houyujiangjun@qq.com
 * @date 2017年12月4日
 */
public class OTUMsgUtil {
    /**
     * 获取需要的tag
     *
     * @param tag            , e.g:101,30d等,忽略大小写
     * @param msg list
     * @return
     */
    public static String getValue(String tag, Message msg) {
        for (TV tv : msg.getTvList()) {
            if (tag.equalsIgnoreCase(tv.getStrTag())) {
                return tv.getStrValue();
            }
        }
        return null;
    }
}
