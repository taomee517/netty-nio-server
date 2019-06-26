//package com.demo.netty.accptor.service;
//
//import com.blackTea.common.constants.ClientTypeEnum;
//import com.demo.netty.accptor.service.impl.OTUCodecServiceImpl;
//
//public class CodecServiceFactory {
//    public static IByteToNettyMsgCodecService getServiceByReqPort(int reqPort){
//        if(ClientTypeEnum.OTU.getPort()==reqPort){
//            return new OTUCodecServiceImpl();
//        }else {
//            return null;
//        }
//    }
//
//}
