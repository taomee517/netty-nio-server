///**
// *
// */
//package com.demo.netty.accptor.service.impl;
//
//import com.blackTea.common.communicate.netty.config.CommunicationConfig;
//import com.blackTea.common.constants.ClientTypeEnum;
//import com.blackTea.common.constants.tag.BSJTagConstant;
//import com.blackTea.common.eventbus.EventEnum;
//import com.blackTea.common.eventbus.Eventbus;
//import com.blackTea.common.model.Message;
//import com.blackTea.common.model.TV;
//import com.blackTea.common.model.TerminalContext;
//import com.blackTea.util.BytesUtil;
//import com.blackTea.util.StrUtil;
//import com.blackTea.util.serialize.SerializeUtil;
//import com.demo.netty.accptor.constant.MSClientEnum;
//import com.demo.netty.accptor.service.IDeviceConfigService;
//import com.demo.netty.accptor.service.ITerminalService;
//import com.demo.netty.accptor.util.BSJCodecUtil;
//import com.fzk.otu.utils.CollectionUtil;
//import com.fzk.terminal.api.dto.TerminalDataDTO;
//import com.fzk.terminal.api.dto.TerminalLoginDTO;
//import com.fzk.terminal.api.vo.TerminalVO;
//import com.mysirui.springcloud.SpringCloudSdk;
//import com.mysirui.springcloud.api.vo.RespModel;
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import kv.m.KV_Client;
//import kv.m.KV_Terminal;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
///**api
// * @author yu.hou, email: houyujiangjun@qq.com
// * @date 2017年12月4日
// */
//@Slf4j
//public class BSJConfigServiceImpl implements IDeviceConfigService {
//    private static ITerminalService terminalService = (ITerminalService) SpringCloudSdk.connect(ITerminalService.class, MSClientEnum.MS_TERMINAL_CLIENT.getMsName());
//    private static final String UNKOWN_SOFTVERSION = "unknown";
//    /**
//     * 是否是寻址消息
//     *
//     * @param msg
//     * @return
//     */
//    @Override
//    public boolean isAddressMsg(Message msg) {
//        return false;
//    }
//
//    /**
//     * 处理寻址消息
//     *
//     * @param msg
//     * @return
//     */
//    @Override
//    public Message handleAddress(Message msg) {
//        return msg;
//    }
//
//    /**
//     * 当前消息是否是登录消息
//     *
//     * @param msg
//     * @return
//     */
//    @Override
//    public boolean isLoginMsg(Message msg) {
//        return BytesUtil.equals(msg.getFunction(), BSJTagConstant.FUNTYPE_LOGIN);
//    }
//
//    /**
//     * 设备登录,
//     *
//     * @param msg
//     * @return 返回null意味着登录失败, 登录成功, 返回值中应携带clienttype和entityid
//     */
//    @Override
//    public Message login(Message msg) {
//        try {
//            /*IDeviceLoginService_BSJ service = RPC.callSync(IDeviceLoginService_BSJ.class);
//            Message loginResult = service.login(msg);
//            return loginResult;*/
//            TV tv = msg.getTvList().get(0);
//            ByteBuf byteBuf = Unpooled.copiedBuffer(tv.getValue());
//            byte[] iccid = new byte[8];
//            byteBuf.readBytes(iccid);
//            String iccidStr = String.valueOf(Long.parseLong(BytesUtil.bcd2Str(iccid)));
//            String imei = iccidStr;
//            if (StrUtil.isBlank(imei)) {
//                log.error("imei不存在,{}", msg);
//                return null;
//            }
//            log.info("博实结设备登陆请求,imei:{}", imei);
//
//            TerminalLoginDTO dto = new TerminalLoginDTO();
//            dto.setImei(imei);
//            dto.setSoftVersion(UNKOWN_SOFTVERSION);
//            RespModel<TerminalVO> login = terminalService.login(dto);
//            if (login == null || login.getEntity() == null) {
//                log.info("bsj登录失败");
//                return null;
//            }
//
//            TerminalVO terminalVO = login.getEntity();
//            //更新KV_terminal
////            KV_Terminal ter = KV_Terminal.dao.find(terminalVO.getClientType(), terminalVO.getId());
////            if (Objects.isNull(ter)) {
////                ter = new KV_Terminal();
////            }
////            ter.setClientType(terminalVO.getClientType())
////                    .setEntityid(terminalVO.getId())
////                    .setTerminalid(terminalVO.getId())
////                    .setLevelcode(terminalVO.getLevelCode())
////                    .setImei(imei);
////            ter.saveOrUpdate(terminalVO.getClientType(), terminalVO.getId());
//            //更新设备客户端状态
//            KV_Client client = KV_Client.dao.find(terminalVO.getClientType(), terminalVO.getId());
//            if (Objects.isNull(client)) {
//                client = new KV_Client();
//            }
//            client.setLoginTime(System.currentTimeMillis()).setIsOnline(true).saveOrUpdate(terminalVO.getClientType(), terminalVO.getId());
//            Message result = msg.clone();
//            //将设备的imei设置到message中
//            result.addTv(new TV("101".getBytes(), imei.getBytes()));
//            result.setClientType(ClientTypeEnum.BSJ.getClientType());
//            //TODO 暂时先不验证.直接存到数据库.
//            result.setEntityID(Integer.parseInt(String.valueOf(terminalVO.getId())));
//            result.setLevelcode(terminalVO.getLevelCode());
//            return result;
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        return null;
//
//    }
//
//    /**
//     * 有的时候 离线统计是很重要的功能, 因此 设备会在心跳超过 一段时期后主动更新数据库.
//     * 但这个时间需要单独配置, 有时候统计不需要实时, 降低频率可以降低对关系型数据库update时的性能冲击
//     *
//     * @return 心跳超时时间, 单位 毫秒, 如果为0, 代表不开启
//     */
//    @Override
//    public long getTTL4RDBLastMsgTime() {
//        return 10 * 60 * 1000L;//十分钟一次 更新数据库中lastmsgtime字段
//    }
//
//    /**
//     * 各自实现更新关系型数据库的最后消息时间
//     *
//     * @param ClientType
//     * @param entityID
//     */
//    @Override
//    public void updateRDBLastMsgTime(int ClientType, int entityID) {
//        try {
//            //IDeviceLoginService_BSJ service = RPC.callSync(IDeviceLoginService_BSJ.class);
//            //service.updateLastMsgTime(entityID);
//            //原business中处理
//            //E_TbTerminal.dao.findById(terminalID).setLast_msg_time(new Date()).update();
//            KV_Client client = KV_Client.dao.find(ClientType, entityID);
//            if (Objects.isNull(client)) {
//                client = new KV_Client();
//            }
//            client.setLastMsgTime(System.currentTimeMillis()).saveOrUpdate(ClientType, entityID);
//            //更新tb_terminal数据库
//            TerminalDataDTO dataDTO = new TerminalDataDTO();
//            dataDTO.setLastMsgTime(new Date());
//            dataDTO.setId(entityID);
//            RespModel<Boolean> booleanRespModel = terminalService.updateData(dataDTO);
//            if (Objects.nonNull(booleanRespModel) && booleanRespModel.getEntity()) {
//                log.info("bsj 设备更新terminal表中LastMsgTime字段成功");
//            } else {
//                log.info("bsj 设备更新terminal表中LastMsgTime字段失败");
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//    }
//
//    /**
//     * ttl 和其它一些重要消息是在ClientTypeEnum中配置的
//     *
//     * @return
//     */
//    @Override
//    public ClientTypeEnum getClientTypeEnum() {
//        return ClientTypeEnum.BSJ;
//    }
//
//    /**
//     * 登录成功事件, 异步调用
//     *
//     * @param msg
//     */
//    @Override
//    public void onLogIn(Message msg) {
//        try {
//            //IDeviceLoginService_BSJ service = RPC.callAsync(IDeviceLoginService_BSJ.class);
//            //service.onLogIn(msg);
//            KV_Client kv = new KV_Client();
//            kv.setIsOnline(true).setLastMsgTime(System.currentTimeMillis()).setLoginTime(System.currentTimeMillis()).saveOrUpdate(msg.getClientType(), msg.getEntityID());
//            Eventbus.me().post(EventEnum.event_login, msg, TerminalContext.me().get(msg.getClientType(), msg.getEntityID()), kv);
//            //由于需要去掉business，则将登录的business业务加到此处---//查询配置信息
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//    }
//
//    /**
//     * 登出事件. 异步调用
//     *
//     * @param msg
//     */
//    @Override
//    public void onLogOut(Message msg) {
//        try {
//            //IDeviceLoginService_BSJ service = RPC.callSync(IDeviceLoginService_BSJ.class);
//            //service.onLogOut(msg);
//            log.info("收到登出事件,{}", msg);
//            KV_Terminal kv_terminal = new TerminalContext().get(msg.getClientType(), msg.getEntityID());
//            TerminalLoginDTO dto = new TerminalLoginDTO();
//            dto.setImei(kv_terminal.getImei());
//            dto.setSoftVersion(UNKOWN_SOFTVERSION);
//            RespModel<Boolean> loginOut = terminalService.loginOut(dto);
//            if (loginOut == null || loginOut.getEntity() == null) {
//                log.info("bsj登出失败");
//                return;
//            }
//            KV_Client kv = new KV_Client();
//            kv.setIsOnline(false);
//            List<byte[]> extend = msg.getExtend();
//            if (!CollectionUtil.isEmpty(extend)) {
//                Map<String, Boolean> map = SerializeUtil.parseByte(extend.get(0));
//                boolean isReplaced = map.get(CommunicationConfig.NETTY_SESSIONKEY_isReplaced);
//                if (!isReplaced) {
//                    kv.saveOrUpdate(msg.getClientType(), msg.getEntityID());
//                } else {
//                    log.info("链接被踢:{}", msg);
//                }
//            }else {
//                log.info("链接被踢:{}", msg);
//            }
//            Eventbus.me().post(EventEnum.event_logout, msg, TerminalContext.me().get(msg.getClientType(), msg.getEntityID()), kv);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public Message getResponseMsg(Message msg) {
//        if (BSJCodecUtil.isVirtualHeart(msg)) {
//            return null;
//        }
//
//        byte[] function = msg.getFunction();
//        //通用下发,要去掉tvlist
//        Message resp = msg.clone();//深度赋值一个对象
//        resp.setTvList(null);
//        return resp;
//    }
//}
