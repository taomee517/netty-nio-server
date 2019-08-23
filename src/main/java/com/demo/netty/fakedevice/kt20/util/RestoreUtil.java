package com.demo.netty.fakedevice.kt20.util;

import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;


public class RestoreUtil {
    /**开始、结束符*/
    private static final byte SIGN_CODE = 0x7e;

    /**转义字符相关*/
    private static final byte ESCAPE_CHAR_E_SRC = 0x7e;
    private static final byte ESCAPE_CHAR_D_SRC = 0x7d;
    private static final byte[] ESCAPE_CHAR_D_TRANS = {0x7d,0x01};
    private static final byte[] ESCAPE_CHAR_E_TRANS = {0x7d,0x02};

    /**
     * 还原转义前的字节数组
     * @param bytes
     * @return
     */
    public static byte[] restore(byte[] bytes){
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byte[] temp = new byte[2];
        byte pre = 0;
        for(int i=0;i<bytes.length;i++){
            byte curr = bytes[i];
            temp[0] = pre;
            temp[1] = curr;
            //如果出现转义的0x7e，则将前一个byte换成0x7e
            if(ArrayUtils.isEquals(ESCAPE_CHAR_E_TRANS,temp)){
                byteBuffer.put(byteBuffer.position()-1,ESCAPE_CHAR_E_SRC);
            }else if(!ArrayUtils.isEquals(ESCAPE_CHAR_D_TRANS,temp)){
                //如果出现转义的0x7d，则不做任何操作
                //如果是正常字节，则加入buffer
                byteBuffer.put(curr);
            }
            pre = curr;
        }
        int position = byteBuffer.position();
        byte[] result = new byte[position];
        System.arraycopy(byteBuffer.array(),0,result,0,position);
        return result;
    }

    /**
     * 转义
     * @param bytes
     * @return
     */
    public static byte[] transfer(byte[] bytes){
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length*2);
        byteBuffer.put(SIGN_CODE);
        for(int i=1;i<bytes.length-1;i++){
            byte curr = bytes[i];
            if(curr == ESCAPE_CHAR_D_SRC){
                byteBuffer.put(ESCAPE_CHAR_D_TRANS[0]);
                byteBuffer.put(ESCAPE_CHAR_D_TRANS[1]);
            }else if(curr == ESCAPE_CHAR_E_SRC){
                byteBuffer.put(ESCAPE_CHAR_E_TRANS[0]);
                byteBuffer.put(ESCAPE_CHAR_E_TRANS[1]);
            }else {
                byteBuffer.put(curr);
            }
        }
        byteBuffer.put(SIGN_CODE);
        int position = byteBuffer.position();
        byte[] result = new byte[position];
        System.arraycopy(byteBuffer.array(),0,result,0,position);
        return result;
    }
}
