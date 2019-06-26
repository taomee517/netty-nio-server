/**
 *
 */
package com.demo.netty.accptor.util;

import com.blackTea.common.exception.BlackTeaBaseRuntimeException;
import com.blackTea.common.model.Message;
import com.blackTea.common.model.TV;
import com.blackTea.util.StrUtil;
import com.blackTea.util.log.LogUtil;
import com.demo.netty.accptor.model.MsgSerializedResult;
import com.fzk.otu.utils.CRCUtil;
import io.netty.buffer.ByteBuf;

/**
 * @author yu.hou, email: houyujiangjun@qq.com
 * @date 2017年11月30日
 */
public class OTUCodecUtil {
    private static org.slf4j.Logger logger = LogUtil.getLogger(OTUCodecUtil.class);
    private static final byte leftWrap = '(';
    private static final byte rightWrap = ')';

    public static void main(String args[]) throws Exception {
//		String in = "(*7c|a3|106,201|101,869651023637275|102,460079241205511|103,898600D23113837|104,otu.ost,01022300|105,a1,18|622,a1c2|)";
        String in = "*ff|DdEmmbY1:4_P{KFt;>#fiusL,wmw_kvyfup&cM&J<uoi.sihi&a2w{bEe4Q%}IJ&<28egQ%[jp>c1379A|DEpbc1.{HP[Mp;Md#Nu$6=,RM$e4wo[:";
        Message msg = deSerialize(in).getMsg();
        System.err.println(msg);
        System.err.println(serialize(msg));
        System.err.println(in);
    }

    /**
     * 是否是登录消息
     *
     * @param msg
     * @return
     */
    public static boolean isAddressMsg(Message msg) {
        if (isHeartMsg(msg)) {//心跳消息
            return false;
        }
        String tag = new String(msg.getFunction());
        return tag.equalsIgnoreCase("a1");
    }

    /**
     * 是否是登录消息
     *
     * @param msg
     * @return
     */
    public static boolean isLoginMsg(Message msg) {
        if (isHeartMsg(msg)) {//心跳消息
            return false;
        }
        String tag = new String(msg.getFunction());
        return tag.equalsIgnoreCase("a3");
    }

    /**
     * 是否是心跳消息
     *
     * @param msg
     * @return
     */
    public static boolean isHeartMsg(Message msg) {
        return null == msg || null == msg.getFunction();
    }

    /**
     * 将文本转化为nettyMsg
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static MsgSerializedResult deSerialize(String in) throws Exception {
        try {
            if (StrUtil.isBlank(in)) {
                return null;
            }
            String strData;
            //是否是加密消息:以*开头，并且消息头不为|a1|
            boolean isEncryptMsg = in.startsWith("*") && !"|a1|".equalsIgnoreCase(in.substring(3,7));
            //寻址消息没有加密, 其它消息都是加密的 (*b0|a1|1
            if (!isEncryptMsg && StrUtil.split(in, "*").get(1).substring(3, 5).equalsIgnoreCase("a1") ) {
                strData = in;
            } else {
                if (isEncryptMsg) {
                    strData = CRCUtil.asiccCrcDecode(in);
                } else {
                    strData = CRCUtil.isEncoded(in);
                }
            }
            if (StrUtil.isBlank(strData)) {
                return null;
            }
            String[] reqArr = strData.split("\\|");
            if (isEncryptMsg) {
                if (!validateCRC(strData, reqArr)) {
                    throw new BlackTeaBaseRuntimeException("校验失败:" + strData);
                }
            }

            byte[] function = reqArr[1].getBytes();
            Message result = new Message();
            result.setFunction(function);

            for (int i = 2; i < reqArr.length; ++i) {
                String[] content = reqArr[i].split(",");
                String tag = content[0];
                if (tag.equals(")")) {
                    break;
                }
                String value = "";
                if (reqArr[i].indexOf(',') != -1) {
                    value = reqArr[i].substring(reqArr[i].indexOf(',') + 1);
                }
                result.getTvList().add(new TV(tag.getBytes(), value.getBytes()));
            }
            return new MsgSerializedResult(strData, result);
        } catch (Exception e) {
            throw e;
        }
    }

    private static boolean validateCRC(String in, String[] reqArr) {
        String crc = reqArr[0].substring(1);
        String body = in.split("\\" + reqArr[0] + "\\|")[1];
        byte[] b = body.getBytes();
        byte crcByte = 0;
        for (int j = 0; j < b.length; ++j) {
            crcByte += b[j];
        }
        if (!CRCUtil.getCRCByteHexValue(crcByte).equals(crc)) {
            return false;
        }
        return true;
    }

    /**
     * 生成原生的响应消息
     *
     * @param outMsg
     * @return
     */
    private static String generateResponseStr(Message outMsg) {
        StringBuilder sb = new StringBuilder(new String(outMsg.getFunction()));
        sb.append("|");
        for (TV tv : outMsg.getTvList()) {
            sb.append(tv.getStrTag()).append(",").append(tv.getStrValue()).append("|");
        }
        return sb.toString();
    }

    /**
     * 序列化，把对象转成字符串
     */
    public static String serialize(Message outMsg) throws Exception {
        try {
            //			if (null == outMsg || outMsg.getTvList().isEmpty()) {//心跳消息
            //				logger.error("无法发送空的消息,{}", outMsg);
            //				return null;
            //			}
            if (OTUCodecUtil.isHeartMsg(outMsg)) {//是心跳消息.
                return "";
            }
            String reponseStr = generateResponseStr(outMsg);
            String result = StrUtil.joint(CRCUtil.getCRCByteStr(reponseStr), reponseStr);
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * bytebuf 装维ascii码  字符串
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static String Byte2StringSerialize(ByteBuf in) throws Exception {
        int length = in.readableBytes();
        if (length < 2) {
            return null;
        }
        int readStart = in.readerIndex();
        int startIndex = -1;
        int endIndex = -1;
        byte currentByte = 0;
        for (int index = 0; index < length; index++) {
            currentByte = in.readByte();
            if (leftWrap == currentByte) {
                startIndex = index;
            } else if (startIndex >= 0 && rightWrap == currentByte) {
                endIndex = index;
                int contentLength = endIndex - startIndex - 1;
                in.readerIndex(readStart + startIndex + 1);
                byte[] result = new byte[contentLength];
                in.readBytes(result, 0, contentLength);
                in.readByte();
                return new String(result, "UTF-8");
            }
        }
        if (startIndex >= 0) {
            in.readerIndex(readStart + startIndex);
        }
        return null;
    }

}
