package com.demo.netty.fakedevice.kt20.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/4/18
 * @time 9:21
 */
public class BytesUtil {
    public static final int MAX_TWO_BYTES_INT = 65535;
    public static final String HEXES = "0123456789ABCDEF";

    public BytesUtil() {
    }

//    public static void main(String[] args) throws UnsupportedEncodingException {
//        String content = "3131313131313131";
//        byte[] bc = new byte[content.length() / 2];
//
//        for(int i = 0; i < content.length() / 2; ++i) {
//            String tc = content.substring(i * 2, (i + 1) * 2);
//            int a = Integer.parseInt(tc, 16);
//            bc[i] = (byte)a;
//        }
//
//        System.err.println(new String(bc, "utf-8"));
//    }

    public static String bytesToHexString4BSJ(byte[] arr) {
        if (arr == null) {
            return "";
        } else {
            StringBuilder hex = new StringBuilder();
            byte[] var2 = arr;
            int var3 = arr.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                byte b = var2[var4];
                hex.append("0123456789ABCDEF".charAt((b & 240) >> 4)).append("0123456789ABCDEF".charAt(b & 15)).append(" ");
            }

            String result = hex.toString();
            return StringUtils.removeEnd(result, "");

        }
    }

    public static String bytesToHexString(byte[] arr) {
        if (arr == null) {
            return "";
        } else {
            StringBuilder hex = new StringBuilder("[");
            byte[] var2 = arr;
            int var3 = arr.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                byte b = var2[var4];
                hex.append("0123456789ABCDEF".charAt((b & 240) >> 4)).append("0123456789ABCDEF".charAt(b & 15)).append(",");
            }

            hex.append("]");
            return hex.toString();
        }
    }

    public static String bytesToHexShortString(byte[] arr) {
        if (arr == null) {
            return "";
        } else {
            StringBuilder hex = new StringBuilder("");
            byte[] var2 = arr;
            int var3 = arr.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                byte b = var2[var4];
                hex.append("0123456789ABCDEF".charAt((b & 240) >> 4)).append("0123456789ABCDEF".charAt(b & 15));
            }

            return hex.toString();
        }
    }

    public static byte[] intToBytes(int value) {
        return new byte[]{(byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value};
    }

    public static byte intToSingleByte(int value) {
        return (byte) value;
    }

    public static int bytesToInt(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 255) << 16 | (bytes[2] & 255) << 8 | bytes[3] & 255;
    }

    public static byte[] intToTwoBytes(int value) {
        if (value <= 65535 && value >= 0) {
            return new byte[]{(byte) (value >> 8), (byte) value};
        } else {
            throw new IllegalArgumentException("数据错误,应为0到65535,当前值:" + value);
        }
    }

    public static int twoBytesToInt(byte[] bytes) {
        if (bytes.length != 2) {
            throw new IllegalArgumentException("只能两个字节,当前:" + bytes.length + "个");
        } else {
            return (bytes[0] & 255) << 8 | bytes[1] & 255;
        }
    }

    public static byte[] intToOneByteArray(int value) {
        if (value <= 255 && value >= 0) {
            return new byte[]{(byte) value};
        } else {
            throw new IllegalArgumentException("数据错误,应为0到255,当前值:" + value);
        }
    }

    public static int oneByteArrayToInt(byte[] bytes) {
        if (bytes.length != 1) {
            throw new IllegalArgumentException("只能一个字节,当前:" + bytes.length + "个");
        } else {
            return bytes[0] & 255;
        }
    }

    public static byte[] longToBytes(long value) {
        byte[] result = new byte[8];

        for (int i = 7; i >= 0; --i) {
            result[i] = (byte) ((int) (value & 255L));
            value >>= 8;
        }

        return result;
    }

    public static long bytesToLong(byte[] b) {
        long result = 0L;

        for (int i = 0; i < 8; ++i) {
            result <<= 8;
            result |= (long) (b[i] & 255);
        }

        return result;
    }

    public static byte[] toPrimitives(Byte[] oBytes) {
        byte[] bytes = new byte[oBytes.length];

        for (int i = 0; i < oBytes.length; ++i) {
            bytes[i] = oBytes[i];
        }

        return bytes;
    }

    public static Byte[] toObjects(byte[] bytesPrim) {
        Byte[] bytes = new Byte[bytesPrim.length];
        int i = 0;
        byte[] var3 = bytesPrim;
        int var4 = bytesPrim.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            byte b = var3[var5];
            bytes[i++] = b;
        }

        return bytes;
    }

    public static byte[] toByteArr(List<Byte> list) {
        return toPrimitives((Byte[]) list.toArray(new Byte[list.size()]));
    }

    public static boolean equals(byte[] arr1, byte[] arr2) {
        return Arrays.equals(arr1, arr2);
    }

    public static boolean equalsIgnoreCase(byte[] arr1, byte[] arr2) {
        return (new String(arr1)).equalsIgnoreCase(new String(arr2));
    }

    public static String bcd2Str(byte[] bytes) {
        StringBuffer temp = new StringBuffer(bytes.length * 2);

        for (int i = 0; i < bytes.length; ++i) {
            temp.append((byte) ((bytes[i] & 240) >>> 4));
            temp.append((byte) (bytes[i] & 15));
        }

        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
    }

    /**从一个byte中取出某几位
     *
     * @param b 源数据byte
     * @param offset 从低到高的下标
     * @return
     */
    public static int getBitFromByte(byte b, int offset){
        int[] datas = new int [8];
        datas[0]= (b & 0x01) == 0x01 ? 1 : 0;
        datas[1]= (b & 0x02) == 0x02 ? 1 : 0;
        datas[2]= (b & 0x04) == 0x04 ? 1 : 0;
        datas[3]= (b & 0x08) == 0x08 ? 1 : 0;
        datas[4]= (b & 0x10) == 0x10 ? 1 : 0;
        datas[5]= (b & 0x20) == 0x20 ? 1 : 0;
        datas[6]= (b & 0x40) == 0x40 ? 1 : 0;
        datas[7]= (b & 0x80) == 0x80 ? 1 : 0;
        return datas[offset];
    }

    public static int allBytesToInt(byte[] bytes) {
//        return bytes[0] << 24 | (bytes[1] & 255) << 16 | (bytes[2] & 255) << 8 | bytes[3] & 255;
        int result = 0;
        for(int i=0;i<bytes.length;i++){
            byte b = bytes[i];
            int intB = b & 0xff;
            int offset = (bytes.length-1-i)*8;
            result = result | (intB << offset) ;
        }
        return result;
    }

    public static long allBytesToLong(byte[] b) {
        long result = 0L;

        for (int i = 0; i < b.length; ++i) {
            result <<= 8;
            result |= (long) (b[i] & 255);
        }

        return result;
    }
}
