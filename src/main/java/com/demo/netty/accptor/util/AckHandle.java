/**
 *
 */
package com.demo.netty.accptor.util;


import com.demo.netty.accptor.constant.GL500Constants;


/**
 * @author jgchen
 */
public class AckHandle extends BaseRespHandle {

    public static boolean isAck(String upMsg) {
        if (upMsg.startsWith("+ACK")) {
            return true;
        }
        return false;
    }

    @Override
    public String getUpMsgPrefix() {
        return GL500Constants.ACK_PREFIX;
    }

}
