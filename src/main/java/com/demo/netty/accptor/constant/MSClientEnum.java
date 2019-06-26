package com.demo.netty.accptor.constant;

/**
 * @author DELL
 * @Title: MSClientEnum
 * @ProjectName spring-cloud-sdk
 * @Description: TODO
 * @date 2019/4/414:03
 */
public enum MSClientEnum {

    MS_TERMINAL_CLIENT("MS_TERMINAL_CLIENT", "设备服务");

    //服务名称
    private String msName;

    //服务描述
    private String msDesc;

    private MSClientEnum(String msName, String msDesc) {
        this.msName = msName;
        this.msDesc = msDesc;
    }

    public String getMsName() {
        return msName;
    }

    public void setMsName(String msName) {
        this.msName = msName;
    }

    public String getMsDesc() {
        return msDesc;
    }

    public void setMsDesc(String msDesc) {
        this.msDesc = msDesc;
    }
}
