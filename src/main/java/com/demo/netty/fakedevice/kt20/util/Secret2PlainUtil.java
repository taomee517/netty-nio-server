package com.demo.netty.fakedevice.kt20.util;

import com.fzk.sdk.util.BytesTranUtil;

import java.nio.ByteBuffer;

import static com.demo.netty.fakedevice.kt20.constant.DtuConstants.*;

public class Secret2PlainUtil {
    public static String secret2Plain(String in ) throws  Exception{
        in = in.replaceAll(" ", "").toUpperCase();

        byte[] bytes = BytesTranUtil.hexStringToBytes(in);

        //转义还原
        bytes = RestoreUtil.restore(bytes);

        //起始符，结束符验证，判断消息合法性和完整性
        ValidateUtil.validate(bytes);

        //消息id
        byte[] srcMsgId = ParseUtil.getMsgId(bytes);

        //设备imei
        byte[] srcSn = ParseUtil.getSn(bytes);

        //消息序列号
        byte[] srcSerialNo = ParseUtil.getSerialNo(bytes);;

        //消息体长度，截取消息体
        byte[] srcProp = new byte[CONTENT_PROPERTIES_LENGTH];
        System.arraycopy(bytes,CONTENT_PROPERTIES_INDEX,srcProp,0,srcProp.length);
        int prop = BytesUtil.twoBytesToInt(srcProp);
        int pack = prop >> 13 & 0x01;
        int encodeType = prop >> 10 & 0x07;
        int aesEncode = prop >> 11 & 1;
        int contentLength = prop & 0x1ff;
        int contentIndex = (pack==1)?(MIN_LENGTH-2+4):(MIN_LENGTH-2);

        byte[] content = new byte[contentLength];
        System.arraycopy(bytes,contentIndex,content,0,contentLength);
//        String encodeContent = BytesTranUtil.toHexString(content);

        boolean isAesMsg = (aesEncode == 1);
        if(isAesMsg){
            //解密原消息内容
            byte[] newContent = AESUtil.decrypt(content);

            ByteBuffer bf = ByteBuffer.allocate(1024);
            //放入msgId
            bf.put(srcMsgId);

            //生成新的消息属性
            byte[] newProp = generateNewProperty(newContent, !isAesMsg);
            bf.put(newProp);

            //放入sn
            bf.put(srcSn);

            //放入序列号
            bf.put(srcSerialNo);

            //放入新的消息内容
            bf.put(newContent);


            //计算新的校验码
            byte[] validContent = ParseUtil.getReadableBytes(bf);
            byte validCode = ValidateUtil.getValidCode(validContent);

            bf.clear();
            bf.put(SIGN_CODE);
            bf.put(validContent);
            bf.put(validCode);
            bf.put(SIGN_CODE);
            byte[] newMsg = ParseUtil.getReadableBytes(bf);
            return BytesUtil.bytesToHexShortString(newMsg);
        }else {
            return in;
        }
    }


    public static String plain2Secret(String in) throws  Exception{
        in = in.replaceAll(" ", "").toUpperCase();

        byte[] bytes = BytesTranUtil.hexStringToBytes(in);

        //转义还原
        bytes = RestoreUtil.restore(bytes);

        //起始符，结束符验证，判断消息合法性和完整性
        ValidateUtil.validate(bytes);

        //消息id
        byte[] srcMsgId = ParseUtil.getMsgId(bytes);

        //设备imei
        byte[] srcSn = ParseUtil.getSn(bytes);

        //消息序列号
        byte[] srcSerialNo = ParseUtil.getSerialNo(bytes);;

        //消息体长度，截取消息体
        byte[] srcProp = new byte[CONTENT_PROPERTIES_LENGTH];
        System.arraycopy(bytes,CONTENT_PROPERTIES_INDEX,srcProp,0,srcProp.length);
        int prop = BytesUtil.twoBytesToInt(srcProp);
        int pack = prop >> 13 & 0x01;
        int encodeType = prop >> 10 & 0x07;
        int aesEncode = prop >> 11 & 1;
        int contentLength = prop & 0x1ff;
        int contentIndex = (pack==1)?(MIN_LENGTH-2+4):(MIN_LENGTH-2);

        byte[] content = new byte[contentLength];
        System.arraycopy(bytes,contentIndex,content,0,contentLength);

        boolean isAesMsg = (aesEncode == 1);
        if(!isAesMsg){
            //解密原消息内容
            byte[] newContent = AESUtil.encrypt(content);

            ByteBuffer bf = ByteBuffer.allocate(1024);
            //放入msgId
            bf.put(srcMsgId);

            //生成新的消息属性 改变加密方式
            byte[] newProp = generateNewProperty(newContent,true);
            bf.put(newProp);

            //放入sn
            bf.put(srcSn);

            //放入序列号
            bf.put(srcSerialNo);

            //放入新的消息内容
            bf.put(newContent);


            //计算新的校验码
            byte[] validContent = ParseUtil.getReadableBytes(bf);
            byte validCode = ValidateUtil.getValidCode(validContent);

            //重新组装消息体
            bf.clear();
            bf.put(SIGN_CODE);
            bf.put(validContent);
            bf.put(validCode);
            bf.put(SIGN_CODE);
            byte[] newMsg = ParseUtil.getReadableBytes(bf);
            return BytesUtil.bytesToHexShortString(newMsg);
        }else {
            return in;
        }
    }


    //是否转密文
    private static byte[] generateNewProperty(byte[] content, boolean toAesMsg){
        int pack = 0;
        int encodeType = toAesMsg ? 2 : 0;
        int length = content.length;
        int msgInfo = pack << 13 | ((encodeType & 0x07) << 10) | length & 0x3ff;
        return BytesUtil.intToTwoBytes(msgInfo);
    }
}
