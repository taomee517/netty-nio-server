package com.demo.netty.fakedevice.kt20.util;

import lombok.extern.slf4j.Slf4j;

import static com.demo.netty.fakedevice.kt20.constant.DtuConstants.*;

@Slf4j
public class ValidateUtil{

    public static byte getValidCode(byte[] content){
        int code = 0;
        for(int i=0; i<content.length;i++){
            byte b = content[i];
            if (i==0) {
                code = b;
            }else {
                code = code ^ b;
            }
        }
        return (byte)code;
    }


    public static boolean validConfirm(byte[] msg){
        byte srcValid = msg[msg.length-2];
        byte[] validContent = new byte[msg.length-3];
        System.arraycopy(msg,1,validContent,0,validContent.length);
        byte validCode = ValidateUtil.getValidCode(validContent);
        log.debug("正确的校验码是: {}", ZeroFillStrUtil.zeroFillStr(Integer.toHexString(validCode & 0xff),2));
        Boolean flag = false;
        if(srcValid==validCode){
            flag = true;
        }
        return flag;
    }


    /**消息合法性和完整性校验
     *
     * @param bytes 消息内容
     * @throws Exception 异常
     */
    public static void validate(byte[] bytes) throws Exception{
        if(bytes.length < MIN_LENGTH){
            log.info("非法的消息: 消息长度错误");
            throw new Exception("非法的消息: 消息长度错误");
        }
        byte startSign = bytes[0];

        if(SIGN_CODE != startSign){
            log.info("非法的消息: 起始位错误");
            throw new Exception("非法的消息: 起始位错误");
        }
        byte endSign = bytes[bytes.length-1];
        if(SIGN_CODE != endSign){
            log.info("非法的消息: 停止位错误，消息内容不完整");
            throw new Exception("非法的消息: 停止位错误，消息内容不完整");
        }
        boolean isValid = ValidateUtil.validConfirm(bytes);
        if(!isValid){
            log.error("非法的消息: {}，消息校验未通过", BytesUtil.bytesToHexShortString(bytes));
            throw new Exception("非法的消息: 消息校验未通过");
        }
    }

}
