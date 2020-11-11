//package com.demo.netty.fakedevice.kt20.util;
//
//import com.demo.netty.fakedevice.kt20.constant.DownMsgType;
//import com.demo.netty.fakedevice.kt20.constant.UpMsgType;
//import com.fzk.sdk.util.BytesTranUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//
//import static com.demo.netty.fakedevice.kt20.constant.DtuConstants.*;
//
//
//@Slf4j
//public class ProtocolSwitchUtil {
//
//    public static String bsj2Fzk(String in) throws Exception{
//        if(StringUtils.isEmpty(in)){
//            return null;
//        }
//        in = in.replaceAll(" ", "").toUpperCase();
//        if(!in.startsWith("7E0900") && !in.startsWith("7E8900")){
//            return in;
//        }
//        byte[] bytes = BytesTranUtil.hexStringToBytes(in);
//        //转义还原
//        bytes = RestoreUtil.restore(bytes);
//
//        //第一步：起始符，结束符验证，判断消息合法性和完整性
//        ValidateUtil.validate(bytes);
//
//        //第二步：计算消息体长度，截取消息体
//        byte[] srcProp = new byte[CONTENT_PROPERTIES_LENGTH];
//        System.arraycopy(bytes,CONTENT_PROPERTIES_INDEX,srcProp,0,srcProp.length);
//        int prop = BytesUtil.twoBytesToInt(srcProp);
//        int pack = prop >> 13 & 0x01;
//        int encodeType = prop >> 10 & 0x07;
//        int aesEncode = prop >> 11 & 1;
//        int contentLength = prop & 0x1ff;
//        int contentIndex = (pack==1)?(MIN_LENGTH-2+4):(MIN_LENGTH-2);
//        byte[] content = new byte[contentLength];
//        System.arraycopy(bytes,contentIndex,content,0,contentLength);
//        String bsjMsg = BytesTranUtil.toHexString(content);
//        if(aesEncode == 1){
//            byte[] newContent = AESUtil.decrypt(content);
//            content = newContent;
//        }
//
//        //第三步：将透传消息部分转成fzk格式的报文
//        byte[] srcMsgId = ParseUtil.getMsgId(bytes);
//        int msgId = BytesUtil.twoBytesToInt(srcMsgId);
//        String result = in;
//        if(UpMsgType.PASSTHROUGH.getMsgId()==msgId || DownMsgType.TRANSMIT.getMsgId()==msgId){
//            byte[] fzkContent = new byte[content.length-1];
//            System.arraycopy(content,1,fzkContent,0,fzkContent.length);
//            //41 代表串口类型
//            String fzkMsg = Integer.toHexString(TRANSMIT_TYPE) + "(" + new String(fzkContent) + ")";
//            result = in.replace(bsjMsg.toUpperCase(),fzkMsg);
//        }
//        return result;
//    }
//
//
//
//}
