package com.demo.netty.echo.utils;

public class ZeroFillUtil {

    public static String zeroFillStr(int value, int strLen){
        StringBuilder strValue = new StringBuilder(String.valueOf(value));
        int valueLen = strValue.length();
        if(valueLen<strLen){
            for(int i=0;i<strLen-valueLen;i++){
                strValue.insert(0, "0");
            }
        }
        return strValue.toString();
    }

    public static String zeroFillStr(String value, int strLen){
        int valueLen = value.length();
        if(valueLen<strLen){
            StringBuilder valueBuilder = new StringBuilder(value);
            for(int i = 0; i<strLen-valueLen; i++){
                valueBuilder.insert(0, "0");
            }
            value = valueBuilder.toString();
        }else{
            value = value.substring(valueLen-strLen);
        }
        return value;
    }

    public static String getZeroFilledHex(int value, int offset){
        String hex = Integer.toHexString(value);
        if(hex.length()<offset){
            return zeroFillStr(hex,offset);
        }
        return hex;
    }

}
