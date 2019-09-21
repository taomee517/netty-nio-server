package com.demo.netty.fakedevice.kt20.util;

import com.fzk.sdk.constant.SpecialData;
import org.apache.commons.lang3.StringUtils;

import java.nio.ByteBuffer;

import static com.demo.netty.fakedevice.kt20.constant.DtuConstants.*;
import static com.fzk.sdk.constant.SpecialData.UNKNOWN_DATA;

public class ParseUtil {
    public static final String HEX_STATUS_TRUE = "1";
    public static final String HEX_STATUS_FALSE = "2";


    /**将Hex字符串状态转为boolean型
     *
     * @param status
     * @return
     */
    public static Boolean parseStatusToBoolean(String status){
        if(StringUtils.isBlank(status)){
            return null;
        }
        if(HEX_STATUS_TRUE.equals(status)){
            return true;
        }else if(HEX_STATUS_FALSE.equals(status)){
            return false;
        }else{
            return null;
        }
    }

    public static int parseStatus(String status){
        if(HEX_STATUS_TRUE.equals(status)){
            return SpecialData.TRUE;
        }else if(HEX_STATUS_FALSE.equals(status)){
            return SpecialData.FALSE;
        }else{
            return UNKNOWN_DATA;
        }
    }

    public static int parseStatusWithOffset(String allInValue,int poisition){
        if (StringUtils.isEmpty(allInValue)) {
            return UNKNOWN_DATA;
        } else if(allInValue.length()<poisition){
            return UNKNOWN_DATA;
        } else{
            String status = allInValue.substring(poisition-1,poisition);
            return parseStatus(status);
        }
    }


    public static byte[] getSn(byte[] bytes){
        byte[] srcPhone = new byte[SN_LENGTH];
        System.arraycopy(bytes,SN_INDEX,srcPhone,0,SN_LENGTH);
        return srcPhone;
    }

    public static byte[] getSerialNo(byte[] bytes){
        byte[] srcSerial = new byte[SERIAL_NO_LENGTH];
        System.arraycopy(bytes,SERIAL_NO_INDEX,srcSerial,0,SERIAL_NO_LENGTH);
        return srcSerial;
    }

    public static byte[] getMsgId(byte[] bytes){
        byte[] srcMsgId = new byte[MESSAGE_TYPE_LENGTH];
        System.arraycopy(bytes,MESSAGE_TYPE_INDEX,srcMsgId,0,srcMsgId.length);
        return srcMsgId;
    }

    public static void putTwoBytesInt(ByteBuffer bf, int value){
        bf.put(BytesUtil.intToTwoBytes(value));
    }

    public static byte[] getReadableBytes(ByteBuffer bf){
        byte[] validateContent = new byte[bf.position()];
        bf.flip();
        bf.get(validateContent);
        return validateContent;
    }

    public static byte[] repackContent(byte[] validateContent, byte validCode){
        //重新拼装
        byte[] content =  new byte[validateContent.length + 3];
        content[0] = SIGN_CODE;
        System.arraycopy(validateContent,0,content,1,validateContent.length);
        content[content.length -2] = validCode;
        content[content.length -1 ] = SIGN_CODE;
        return content;
    }
}
