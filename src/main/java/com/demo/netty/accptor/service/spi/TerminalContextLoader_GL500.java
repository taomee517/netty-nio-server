//package com.demo.netty.accptor.service.spi;
//
//import com.alibaba.fastjson.JSON;
//import com.blackTea.common.eventbus.spi.ITerminalContextLoader;
//import com.demo.netty.accptor.constant.MSClientEnum;
//import com.demo.netty.accptor.service.ITerminalService;
//import com.fzk.terminal.api.vo.TerminalVO;
//import com.mysirui.springcloud.SpringCloudSdk;
//import com.mysirui.springcloud.api.vo.RespModel;
//import kv.m.KV_Terminal;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.Objects;
//
///**
// * @PROJECT:blacktea-acceptor-otu
// * @Author:zhangxia
// * @Desc:
// * @Date: 13:25 2019/5/22
// * @VERSION: 2.0
// */
//@Slf4j
//public class TerminalContextLoader_GL500 implements ITerminalContextLoader {
//    private static final String UNKOWN_SOFTVERSION = "unknown";
//    private static ITerminalService terminalService = (ITerminalService) SpringCloudSdk.connect(ITerminalService.class, MSClientEnum.MS_TERMINAL_CLIENT.getMsName());
//
//    public TerminalContextLoader_GL500() {
//    }
//
//    @Override
//    public KV_Terminal load(int clientType, int entityID) {
//        KV_Terminal ter = new KV_Terminal();
//        ter.setClientType(clientType);
//        ter.setEntityid(entityID);
//        //E_TbTerminal entity = (E_TbTerminal)E_TbTerminal.dao.findById(entityID);
//        RespModel<TerminalVO> info = terminalService.getInfo(entityID);
//        if (Objects.nonNull(info.getEntity())) {
//            TerminalVO terminalVO = info.getEntity();
//            log.info("成功获取到设备信息--{}", JSON.toJSONString(terminalVO));
//            ter.setBarcode(terminalVO.getBarCode());
//            //ter.setFirst_msg_time(terminalVO.get());
//            ter.setIccid(terminalVO.getIccId());
//            ter.setImei(terminalVO.getImei());
//            ter.setLevelcode(terminalVO.getLevelCode());
//            //ter.setMsisdn(terminalVO.get());
//            ter.setClientType(terminalVO.getClientType());
//            ter.setEntityid(entityID);
//            ter.setTerminalid(entityID);
//            ter.setVehicle_id(terminalVO.getVehicleId());
//        }
//        return ter;
//    }
//}
