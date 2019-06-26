//package com.demo.netty.accptor.service;
//
//import com.blackTea.common.constants.ClientTypeEnum;
//import com.demo.netty.accptor.service.impl.OTUConfigServiceImpl;
//
//public class DeviceConfigFactory {
//    public static IDeviceConfigService getServiceByReqPort(int reqPort){
//        if(ClientTypeEnum.OTU.getPort()==reqPort){
//            return new OTUConfigServiceImpl();
//        }else {
//            return null;
//        }
//    }
//
//}
