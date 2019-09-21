package com.demo.netty.fakedevice.kt20.util;

public class ZeroFillStrUtil {

    public static String zeroFillStr(int value,int strLen){
        String strValue = String.valueOf(value);
        int valueLen = strValue.length();
        if(valueLen<strLen){
            for(int i=0;i<strLen-valueLen;i++){
                strValue = "0" + strValue;
            }
        }
        return strValue;
    }

    public static String zeroFillStr(String value,int strLen){
        int valueLen = value.length();
        if(valueLen<strLen){
            for(int i=0;i<strLen-valueLen;i++){
                value = "0" + value;
            }
        }
        return value;
    }

}
