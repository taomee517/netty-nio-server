/**
 *
 */
package com.demo.netty.accptor.util;

/**
 * @author luosanjun
 */
public class BytesTranUtil {

    /*
     * short 与 byte[]转化
     */
    public static byte[] shortToBytes(int value) {
        //	return ByteBuffer.allocate(4).putInt(value).array();//略为低效
        return new byte[]{(byte) (value >> 8), (byte) value};
    }

    public static int bytesToShort(byte[] bytes) {
        return (bytes[0] & 0xFF) << 8 | (bytes[1] & 0xFF);
    }

    /**
     * 转化int为byte
     *
     * @param value
     * @return
     */
    public static byte[] intToBytes(int value) {
        //	return ByteBuffer.allocate(4).putInt(value).array();//略为低效
        return new byte[]{(byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value};
    }

    public static int bytesToInt(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }

    /**
     * 转换long为bytearry
     *
     * @param number
     * @return
     */
    public static byte[] longToBytes(long value) {
        //return ByteBuffer.allocate(8).putLong(value).array();//略为低效
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (value & 0xFF);
            value >>= 8;
        }
        return result;
    }

    public static long bytesToLong(byte[] b) {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 8;
            result |= (b[i] & 0xFF);
        }
        return result;
    }

    /**
     * 据说32位机器需要这样玩，short占2个字节,高位前,低位后
     *
     * @param src
     * @param offset
     * @return
     */
    public static int bytesToShort(byte[] src, int offset) {
        int value;
        value = (src[offset] & 0xFF) << 8 | (src[offset + 1] & 0xFF);
        return value;
    }

    public static int bytesToInt(byte[] bytes, int offset) {
        return bytes[offset] << 24 | (bytes[offset + 1] & 0xFF) << 16 | (bytes[offset + 2] & 0xFF) << 8 | (bytes[offset + 3] & 0xFF);
    }

    /**
     * 高位在前，低位在后
     *
     * @param data
     * @return
     */
    public static byte[] intToBytesFor32(int data) {
        byte[] src = new byte[2];
        src[0] = (byte) ((data >> 8) & 0xFF);
        src[1] = (byte) (data & 0xFF);
        return src;
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 往byte数组中插入数据
     *
     * @param src
     * @param data
     * @param count
     * @return 返回偏移量
     */
    public static Integer insert(byte[] src, byte[] data, Integer count) {
        for (int i = 0; i < data.length; i++) {
            src[count] = data[i];
            count++;
        }
        return count;
    }

    public static String get32BitBinString(int number) {
        return getBitStr(number, 32);
    }

    public static String get16BitBinString(int number) {
        return getBitStr(number, 16);
    }

    public static String getBitStr(int number, int size) {
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sBuilder.append(number & 1);
            number = number >>> 1;
        }
        return sBuilder.reverse().toString();
    }

    /**
     * @功能: BCD码转为10进制串(阿拉伯数据)
     * @参数: BCD码
     * @结果: 10进制串
     */
    public static String bcd2Str(byte[] bytes) {
        StringBuffer temp = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
            temp.append((byte) (bytes[i] & 0x0f));
        }
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
    }

    /**
     * @功能: 10进制串转为BCD码
     * @参数: 10进制串
     * @结果: BCD码
     */
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }
        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    public static byte getXor(byte[] datas) {
        byte temp = datas[0];
        for (int i = 1; i < datas.length; i++) {
            temp ^= datas[i];
        }
        return temp;
    }

    /**
     * 二进制转10进制
     *
     * @return
     */

    public int[] BinstrToIntArray(String binStr) {
        char[] temp = binStr.toCharArray();
        int[] result = new int[temp.length];
        for (int i = 0; i < temp.length; i++) {
            result[i] = temp[i] - 48;
        }
        return result;
    }

    public static int getHeight4(int value) {//获取高四位
        int height;
        height = ((value & 0xf0) >> 4);
        return height;
    }

    public static int getLow4(int value) {//获取低四位
        return (value & 0x0f);
    }

    /**
     * 获取bit值,顺序为低位开始
     *
     * @param b
     * @param bitIndex
     * @return
     */
    public static int getBitValue(byte b, int bitIndex) {
        return b >>> bitIndex & 1;
    }

    /**
     * 字节转16进制字符串
     *
     * @param bytes
     * @param index
     * @param lenth
     * @return
     */
    public static String toHexString(byte[] bytes, int index, int lenth, boolean space) {
        StringBuffer result = new StringBuffer(2 * lenth);
        for (int i = 0; i < lenth; i++) {
            String hex = Integer.toHexString(bytes[index + i] & 0xff);
            if (hex.length() == 1) {
                result.append('0');
            }
            result.append(hex);
            if (space) {
                result.append(' ');
            }
        }
        return result.toString();
    }

    public static String toHexString(byte[] bytes, int index, int lenth) {
        return toHexString(bytes, index, lenth, false);
    }

    public static int toHexIntValue(byte[] bytes, int index, int lenth) {

        return Integer.parseInt(toHexString(bytes, index, lenth));
    }

    public static String toHexString(byte[] bytes) {
        return toHexString(bytes, 0, bytes.length, false);
    }

    public static String toHexString(byte value) {
        String s = Integer.toHexString(value & 0xFF);
        if (s.length() == 1) {
            return "0" + s;
        } else {
            return s;
        }
    }

    public static String toHexString(byte[] bytes, boolean space) {
        return toHexString(bytes, 0, bytes.length, space);
    }

    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }
}
