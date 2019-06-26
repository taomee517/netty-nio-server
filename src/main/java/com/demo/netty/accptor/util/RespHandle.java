/**
 *
 */
package com.demo.netty.accptor.util;

import com.blackTea.util.log.LogUtil;
import com.demo.netty.accptor.constant.GL500Constants;

/**
 * @author jgchen
 */
public class RespHandle extends BaseRespHandle {

    private static org.slf4j.Logger logger = LogUtil.getLogger(RespHandle.class);

    public static boolean isResp(String upMsg) {
        if (upMsg.startsWith(GL500Constants.RESP_PREFIX)) {
            return true;
        }
        return false;
    }

    @Override
    public String getUpMsgPrefix() {
        return GL500Constants.RESP_PREFIX;
    }

}
