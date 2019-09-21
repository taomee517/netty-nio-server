package com.demo.netty.fakedevice.kt20.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;


/**
 *
 * @author andy.wang
 * @时间 2016-4-21
 */
@Slf4j
public class AESUtil {

    /**
     * 秘钥长度
     */
    private static final int SECURE_KEY_LENGTH = 16;
    public static final byte[] KEY = {0x20,0x57,0x2F,0x52,0x36,0x4B,0x3F,0x47,0x30,0x50,0x41,0x58,0x11,0x63,0x2D,0x2B};

    /**
     * 采用AES128解密
     *
     * @param data
     * @return
     * @throws Exception
     *             ,Exception
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data)
            throws Exception {
        if (data == null || data.length == 0) {
            return data;
        }

        // 获得密匙数据
        byte[] rawKeyData = getAESKey();
        // 从原始密匙数据创建一个KeySpec对象
        SecretKeySpec key = new SecretKeySpec(rawKeyData, "AES");
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        // 用密匙初始化Cipher对象  ECB mode cannot use IV
//        byte[] iv = new byte[SECURE_KEY_LENGTH];
//        , new IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, key);
        try {
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }

    }

    public static byte[] getAESKey()
            throws UnsupportedEncodingException {
        // Use the first 16 bytes (or even less if key is shorter)
        byte[] keyBytes16 = new byte[SECURE_KEY_LENGTH];
        System.arraycopy(KEY, 0, keyBytes16, 0,
                Math.min(KEY.length, SECURE_KEY_LENGTH));
        return keyBytes16;
    }

    /**
     * 采用AES128加密
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data)
            throws Exception {
        if (data == null || data.length == 0) {
            return data;
        }

        // 获得密匙数据
        byte[] rawKeyData = getAESKey();
        // 从原始密匙数据创建KeySpec对象
        SecretKeySpec key = new SecretKeySpec(rawKeyData, "AES");
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // 正式执行加密操作

        return cipher.doFinal(data);
    }

//    /**
//     * zlib解压
//     *
//     * @param input
//     * @return
//     */
//    public static byte[] unzip(byte[] input) throws Exception {
//        if (input == null || input.length == 0) {
//            return input;
//        }
//
//        byte[] out = null;
//        ByteArrayOutputStream bao = null;
//        Inflater decompresser = new Inflater();
//        try {
//            bao = new ByteArrayOutputStream(input.length);
//            decompresser.setInput(input);
//            byte[] buf = new byte[1024];
//            int len = 0;
//            while (!(decompresser.finished() || decompresser.needsInput())) {
//                len = decompresser.inflate(buf);
//                if (len == 0) {
//                    break;
//                }
//                bao.write(buf, 0, len);
//            }
//            out = bao.toByteArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new Exception("ILLEGAL_COMPRESSION");
//        } finally {
//            try {
//                if (null != bao) {
//                    bao.close();
//                }
//                decompresser.end();
//            } catch (IOException e) {
//                log.error("close unzip stream error:" + e.getMessage());
//            }
//        }
//        return out;
//
//    }
//
//    /**
//     * zlib压缩
//     *
//     * @param input
//     * @return
//     */
//    public static byte[] zip(byte[] input) throws Exception {
//        if (input == null || input.length == 0) {
//            return input;
//        }
//
//        byte[] out = null;
//        ByteArrayOutputStream bao = null;
//        Deflater compresser = new Deflater();
//        try {
//            compresser.setInput(input);
//            compresser.finish();
//            bao = new ByteArrayOutputStream(input.length);
//            byte[] buf = new byte[1024];
//            int len;
//            while (!compresser.finished()) {
//                len = compresser.deflate(buf);
//                bao.write(buf, 0, len);
//            }
//            out = bao.toByteArray();
//        } finally {
//            try {
//                if (null != bao) {
//                    bao.close();
//                }
//                compresser.end();
//            } catch (IOException e) {
//                log.error("close zip stream error:" + e.getMessage());
//            }
//        }
//        return out;
//    }

    public static void main(String[] args) throws Exception {
        String key = new String(KEY,"gb2312");
        System.out.println(key);

    }

}

