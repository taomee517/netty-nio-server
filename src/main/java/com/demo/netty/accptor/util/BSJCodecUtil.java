/**
 *
 */
package com.demo.netty.accptor.util;
import com.blackTea.common.constants.tag.BSJTagConstant;
import com.blackTea.common.model.Message;
import com.blackTea.common.model.TV;
import com.blackTea.util.BytesUtil;
import com.blackTea.util.log.LogUtil;
import com.demo.netty.accptor.constant.BSJConstant;
import com.demo.netty.accptor.model.BSJCodecResult;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.List;

import static com.demo.netty.accptor.constant.BSJConstant.*;

/**
 * @author yu.hou, email: houyujiangjun@qq.com
 * @date 2017年11月30日
 */
public class BSJCodecUtil {
    private static org.slf4j.Logger logger = LogUtil.getLogger(BSJCodecUtil.class);

    public static void main(String args[]) throws Exception {
        //		String in = "(*7c|a3|106,201|101,869651023637275|102,460079241205511|103,898600D23113837|104,otu.ost,01022300|105,a1,18|622,a1c2|)";

        //		Message msg = deSerialize(in);
        //		System.err.println(msg);
        //		System.err.println(serialize(msg));
        //		System.err.println(in);
        //		System.err.println(BytesUtil.bytesToHexString(BytesUtil.intToTwoBytes(-5778)));
        System.out.println((byte) 100);
    }

    /**
     * 序列化，把对象转成字符串
     * 起始位 2 0x78 0x78
     * 包长度 1 0x05
     * 协议号 1 0x01
     * 信息序列号 2 0x00 0x01
     * 错误校验 2 0xD9 0xDC
     * 停止位 2 0x0D 0x0A
     */
    public static byte[] serialize(Message out) {
        try {
            ByteBuf result = Unpooled.buffer();

            byte[] resultData = null;
            if (out.getTvList() != null && out.getTvList().size() > 0) {
                resultData = out.getTvList().get(0).getValue();
            }

            int resultDataLen = 0;
            if (resultData != null) {
                resultDataLen = resultData.length;
            }

            byte[] checkData = new byte[4 + resultDataLen];

            checkData[0] = (byte) (5 + resultDataLen);//包长度

            System.arraycopy(out.getFunction(), 0, checkData, 1, 1);//协议号

            //具体内容，特别是下发的那些
            if (resultDataLen > 0) {
                System.arraycopy(resultData, 0, checkData, 2, resultData.length);//内容
            }

            try {
                System.arraycopy(out.getExtend().get(0), 0, checkData, checkData.length - 2, 2);//信息序列号 2位
            } catch (Exception e) {
                e.printStackTrace();
            }

            result.writeBytes(BSJConstant.START_POSITION);
            result.writeBytes(checkData);
            result.writeShort(calcCRC(checkData));

            byte[] function = out.getFunction();

            result.writeBytes(BSJConstant.STOP_POSITION);

            byte[] content = new byte[result.readableBytes()];

            result.readBytes(content);
            return content;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

//    public static final int MIN_LENTH = 2 + 1 + 1 + 2 + 2 + 2;

    /**
     * 将文本转化为nettyMsg
     *
     * @param in
     * @return BSJCodecResult 如果异常返回null,如果验证非法 返回的message是null.但是str还有
     * @throws Exception
     */
    public static List<BSJCodecResult> deSerialize(ByteBuf in) {
        try {
            if (in.readableBytes() < MIN_LENTH) {
                return null;
            }

            List<BSJCodecResult> codecResults = new ArrayList<>();

            handleMsg(codecResults, in);

            return codecResults;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        } finally {
            in.clear();
        }
    }

    private static void handleMsg(List<BSJCodecResult> codecResults, ByteBuf in) {
        byte[] startPosition = new byte[2];
        in.readBytes(startPosition);
        if (!BytesUtil.equals(startPosition, BSJConstant.START_POSITION)) {
            in.resetReaderIndex();
            logger.error("非法的消息:{}", "起始位错误");
            return;
        }
        // 长度:协议号+信息内容+信息序列号+错误校验  (停止位2不包括)
        byte length = in.readByte();//包长度
        int len = length & 0xff;
        if (in.readableBytes() >= len + 2) {
            byte[] data = new byte[len];
            in.readBytes(data);
            BSJCodecResult result = new BSJCodecResult();
            //				result.setStr(BytesUtil.bytesToHexString(data));
            //				logger.info("收到原始消息->{}", BytesUtil.bytesToHexString(data));
            //functionID  不是拆开的两个十六进制表示的字节, 是合在一起的最大长度为两个字节的四个char的六十进制表达. 不是0x80 0x00 ,而是0x8000
            ByteBuf dataBuf = Unpooled.copiedBuffer(data);

            //协议号
            byte[] protocolNo = new byte[1];
            dataBuf.readBytes(protocolNo);
            //信息内容
            byte[] msgContent = new byte[data.length - 5];
            dataBuf.readBytes(msgContent);

            Message msg = new Message();
            msg.setFunction(protocolNo);

            List<TV> tvs = splitFunctions(protocolNo, msgContent, codecResults);//消息拆分
            //			if (tvs == null || tvs.size() == 0) {
            //				return;
            //			}
            msg.setTvList(tvs);

            //信息序列号
            byte[] sn = new byte[2];
            dataBuf.readBytes(sn);

            //错误校验
            byte[] errValidate = new byte[2];
            dataBuf.readBytes(errValidate);

            //extend
            List<byte[]> extend = new ArrayList<>();
            extend.add(sn);
            msg.setExtend(extend);

            result.setMsg(msg);

            in.readShort();//读取结束符

            codecResults.add(result);

            if (in.readableBytes() > 0) {
                handleMsg(codecResults, in);
            }

        } else {
            in.resetReaderIndex();
        }
    }

    /**
     * 消息类型拆分
     *
     * @param functionNo
     * @return
     */
    private static List<TV> splitFunctions(byte[] functionNo, byte[] data, List<BSJCodecResult> codecResults) {
        logger.info("functionNo={}，信息内容：{},size:{}", BytesTranUtil.toHexString(functionNo, true), BytesTranUtil.toHexString(data, true), data.length);
        List<TV> result = new ArrayList<TV>();

        if (BytesUtil.equals(functionNo, BSJTagConstant.FUNTYPE_ALARM)) {//告警：GPS+告警
            result.add(new TV(String.valueOf(BSJTagConstant.EALARM_GPS).getBytes(), data));
        } else if (BytesUtil.equals(functionNo, BSJTagConstant.FUNTYPE_GPS)) {//GPS
            if (codecResults == null || codecResults.size() == 0) {
                result.add(new TV(String.valueOf(BSJTagConstant.EGPS).getBytes(), data));
            } else {//有可能设备上报一条数据，需要拆分成多条数据时，GPS的数据信息中GPS时间是一样的，要过滤这种情况，因为后端MQ会批量拉取数据消费时，会产生并发问题
                boolean isFilterableGPSData = isFilterableGPSData(data, codecResults);
                if (!isFilterableGPSData) {//在同一次上报消息中，只要有一次gps数据就行了，其它的抛弃掉
                    result.add(new TV(String.valueOf(BSJTagConstant.EGPS).getBytes(), data));
                }
            }
        } else if (BytesUtil.equals(functionNo, BSJTagConstant.FUNTYPE_HEARTBEAT)) {//心跳 处理GSM信号
            //			byte gsmSignal = data[2];
            result.add(new TV(BSJTagConstant.FUNTYPE_HEARTBEAT, data));
        } else if (BytesUtil.equals(functionNo, BSJTagConstant.FUNTYPE_LOGIN)) {
            result.add(new TV(BSJTagConstant.FUNTYPE_LOGIN, data));
        } else if (BytesUtil.equals(functionNo, BSJTagConstant.FUNTYPE_RESP)) {
            result.add(new TV(BSJTagConstant.FUNTYPE_RESP, data));
        }

        return result;
    }

    private static boolean isFilterableGPSData(byte[] data, List<BSJCodecResult> codecResults) {
        for (BSJCodecResult bsjCodecResult : codecResults) {
            List<TV> tvs = bsjCodecResult.getMsg().getTvList();
            for (TV tv : tvs) {
                if (BytesUtil.equals(tv.getTag(), String.valueOf(BSJTagConstant.EGPS).getBytes())) {//判断都是GPS数据
                    return true;
                    //					byte[] currentData = BytesTranUtil.subBytes(data, 0, 6);
                    //					byte[] beforeData = BytesTranUtil.subBytes(tv.getValue(), 0, 6);
                    //
                    //					if (BytesUtil.equals(currentData, beforeData)) {//判断是同一时间
                    //						return true;
                    //					}
                }
            }
        }

        return false;
    }

    /**
     * 是否是合法的消息
     *
     * @param data
     * @return
     */
    public static boolean isValideMsg(byte[] data) {
        if (data.length < 2) {
            return false;
        }
        int crc = BytesUtil.twoBytesToInt(new byte[]{data[data.length - 2], data[data.length - 1]});
        byte[] crcData = new byte[data.length - 2];
        System.arraycopy(data, 0, crcData, 0, data.length - 2);
        return crc == calcCRC(crcData);
    }

    public static int calcCRC(byte[] msg) {
        short crc = (short) 0xFFFF;
        int i, j;
        boolean c15, bit;

        for (i = 0; i < msg.length; i++) {
            for (j = 0; j < 8; j++) {
                c15 = ((crc >> 15 & 1) == 1);
                bit = ((msg[i] >> (7 - j) & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) {
                    crc ^= 0x1021;
                }
            }
        }
        return crc & 0xFFFF;
    }

    public static boolean isVirtualHeart(Message message) {
        String function = new String(message.getFunction());
        if (VIRTUAL_HEART_KEY.equals(function)) {
            return true;
        }
        return false;
    }
}
