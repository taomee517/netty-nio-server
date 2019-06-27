/**
 *
 */
package com.demo.netty.accptor.service.impl;

import com.alibaba.fastjson.JSON;
import com.blackTea.common.acceptor.AcceptorStatus;
import com.blackTea.common.address.AddressBusiness;
import com.blackTea.common.communicate.netty.config.CommunicationConfig;
import com.blackTea.common.constants.ClientTypeEnum;
import com.blackTea.common.eventbus.EventEnum;
import com.blackTea.common.eventbus.Eventbus;
import com.blackTea.common.model.Message;
import com.blackTea.common.model.TV;
import com.blackTea.common.model.TerminalContext;
import com.blackTea.common.msgSender.MsgSender;
import com.blackTea.util.DateTimeUtil;
import com.blackTea.util.StrUtil;
import com.blackTea.util.serialize.SerializeUtil;
import com.demo.netty.accptor.constant.MSClientEnum;
import com.demo.netty.accptor.service.IDeviceConfigService;
import com.demo.netty.accptor.service.ITerminalService;
import com.demo.netty.accptor.session.NettySession;
import com.demo.netty.accptor.util.OTUCodecUtil;
import com.demo.netty.accptor.util.OTUMsgUtil;
import com.fzk.otu.utils.CollectionUtil;
import com.fzk.otu.utils.StringUtils;
import com.fzk.terminal.api.dto.TerminalLoginDTO;
import com.fzk.terminal.api.vo.TerminalVO;
import com.mysirui.springcloud.SpringCloudSdk;
import com.mysirui.springcloud.api.vo.RespModel;
import kv.m.KV_Client;
import kv.m.KV_Terminal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author yu.hou, email: houyujiangjun@qq.com
 * @date 2017年12月4日
 */
@Slf4j
public class OTUConfigServiceImpl implements IDeviceConfigService {

    private static ITerminalService terminalService = (ITerminalService) SpringCloudSdk.connect(ITerminalService.class, MSClientEnum.MS_TERMINAL_CLIENT.getMsName());

    private static final String UNKOWN_SOFTVERSION = "unknown";

    /**
     * 是否是寻址消息
     *
     * @param msg
     * @return
     */
    @Override
    public boolean isAddressMsg(Message msg) {
        return OTUCodecUtil.isAddressMsg(msg);
    }

    /**
     * 处理寻址消息
     *
     * @param msg
     * @return
     */
    @Override
    public Message handleAddress(Message msg) {
        /*IDeviceLoginService_OTU service = RPC.callSync(IDeviceLoginService_OTU.class);
        Message loginResult = service.address(msg);
        return loginResult;
        */
        AcceptorStatus acc = AddressBusiness.me().getAnAvailableAcceptor(msg.getClientType());
        if (acc != null) {
            Message resp = msg.clone();
            resp.setFunction("a2".getBytes());
            String address = acc.getIp() + "," + StrUtil.octal2Hex(acc.getPort());
            /*****************************************************************************************************************************************************
             body.add((short) 0x206, address);
             body.add((short) 0x20f, address + "," + address + ",223.6.255.5,bbb");
             *****************************************************************************************************************************************************/
            List<TV> tvList = new ArrayList<>();
            /*****************************************************************************************************************************************************
             设置AG:
             tvList.add(new TV("206".getBytes(), address.getBytes()));
             tvList.add(new TV("20f".getBytes(), (address + "," + address + ",223.6.255.5,bbb").getBytes()));//始终都要有一个备用地址
             设置AS  621:
             *****************************************************************************************************************************************************/
            tvList.add(new TV("621".getBytes(), address.getBytes()));
            resp.setTvList(tvList);
            return resp;
        }
        return null;

    }

    /**
     * 当前消息是否是登录消息
     *
     * @param msg
     * @return
     */
    @Override
    public boolean isLoginMsg(Message msg) {
        return OTUCodecUtil.isLoginMsg(msg);
    }


    /**
     * 判断当前消息是否透传且需要回复的消息
     */
    public Message getTransResp(Message msg) {
        String func = new String(msg.getFunction());
        if(!"5".equals(func)){
            return null;
        }
        List<TV> tvs = msg.getTvList();
        if(CollectionUtil.isEmpty(tvs)){
            return null;
        }else {
            TV tv = tvs.get(0);
            String tag = tv.getStrTag();
            //e.g: 3,7#b305,0,2,-1,22,2,1#
            String value = tv.getStrValue();
            if(!"614".equals(tag)){
                return null;
            }
            List<String> needAckBfuncArray = Arrays.asList("1","3","7");
            String[] bParas = value.split(",");
            String innerDeviceId = bParas[0];
            String bFuncNtag = bParas[1];
            String bFunc = bFuncNtag.split("#")[0];
            String bTag = bFuncNtag.split("#")[1];
            if(!needAckBfuncArray.contains(bFunc)){
                return null;
            }else {
                StringBuilder sb = new StringBuilder();
                sb.append(innerDeviceId);
                sb.append(",");
                int respFunc = Integer.valueOf(bFunc) + 1;
                sb.append(respFunc);
                sb.append("#");
                sb.append(bTag);
                String respBvalue = sb.toString();
                Message resp = new Message();
                resp.setFunction("5".getBytes());
                TV respTv = new TV("613".getBytes(),respBvalue.getBytes());
                resp.setTvList(Collections.singletonList(respTv));
                return resp;
            }
        }

    }

    /**
     * 设备登录,
     *
     * @param msg
     * @return 返回null意味着登录失败, 登录成功, 返回值中应携带clienttype和entityid
     */
    @Override
    public Message login(Message msg) {
        String imei = OTUMsgUtil.getValue("101", msg);
        if (StrUtil.isBlank(imei)) {
            log.info("没有发现imei信息,msg={}", msg);
            return null;
        } else { //有imei信息
            //TODO 如果单独运行基础组件,需要提供有效的entityID产生机制
            String softVersiong = OTUMsgUtil.getValue("104", msg);
            TerminalLoginDTO dto = new TerminalLoginDTO();
            dto.setImei(imei);
            if (softVersiong == null) {
                softVersiong = UNKOWN_SOFTVERSION;
            } else {
                String[] sv = softVersiong.split(",");
                if (sv.length > 2) {
                    softVersiong = sv[1];
                } else {
                    softVersiong = UNKOWN_SOFTVERSION;
                }
            }
            dto.setSoftVersion(softVersiong);
            RespModel<TerminalVO> login = terminalService.login(dto);
            if (login == null || login.getEntity() == null) {
                log.info("设备没有注册，msg={}", JSON.toJSONString(msg));
                return null;
            }
            TerminalVO tv = login.getEntity();
            msg.setClientType(tv.getClientType());
            msg.setEntityID(tv.getId());
            KV_Terminal ter = KV_Terminal.dao.find(tv.getClientType(), tv.getId());
            if (Objects.isNull(ter)) {
                ter = new KV_Terminal();
            }
            ter.setClientType(tv.getClientType())
                    .setEntityid(tv.getId())
                    .setTerminalid(tv.getId())
                    .setLevelcode(tv.getLevelCode())
                    .setImei(imei);
            ter.saveOrUpdate(tv.getClientType(), tv.getId());

            //返回值
            Message result = new Message();
            result.setClientType(ClientTypeEnum.OTU.getClientType());
            result.setEntityID(ter.getTerminalid());
            result.setLevelcode(ter.getLevelcode());
            return result;
        }
    }

    /**
     * 有的时候 离线统计是很重要的功能, 因此 设备会在心跳超过 一段时期后主动更新数据库.
     * 但这个时间需要单独配置, 有时候统计不需要实时, 降低频率可以降低对关系型数据库update时的性能冲击
     *
     * @return 心跳超时时间, 单位 毫秒, 如果为0, 代表不开启
     */
    @Override
    public long getTTL4RDBLastMsgTime() {
        return 5 * 60 * 1000L;//十分钟一次 更新数据库中lastmsgtime字段
    }

    /**
     * 各自实现更新关系型数据库的最后消息时间
     *
     * @param ClientType
     * @param entityID
     */
    @Override
    public void updateRDBLastMsgTime(int ClientType, int entityID) {
        KV_Terminal ter = KV_Terminal.dao.find(ClientType, entityID);
        if (Objects.isNull(ter)) {
            log.error("kv_terminal不存在");
            return;
        }
        try {
            String imei = ter.getImei();
            if(StringUtils.isEmpty(imei)){
                imei = terminalService.getInfo(entityID).getEntity().getImei();
            }
            RespModel<Boolean> booleanRespModel = terminalService.updateTerminalLastMsg(imei);
            if (Objects.nonNull(booleanRespModel)) {
                log.info("设备id-{}-更新lastmsg结果--{}", ter.getEntityid(), booleanRespModel.getEntity());
            }
        } catch (Exception e) {
            log.error("更新lastmsg异常--{}", e);
        }
       /* if (OTUAcceptorConfig.me().isHasExtendUpDBTimeService()) {
            IUpdateRDBLastMsgTimeService service = SPIServiceFactory.findService(IUpdateRDBLastMsgTimeService.class);
            service.update(ClientType, entityID);
        }*/
    }

    /**
     * ttl 和其它一些重要消息是在ClientTypeEnum中配置的
     *
     * @return
     */
    @Override
    public ClientTypeEnum getClientTypeEnum() {
        return ClientTypeEnum.OTU;
    }

    /**
     * 登录成功事件, 异步调用
     *
     * @param msg
     */
    @Override
    public void onLogIn(Message msg) {
        try {
            /*IDeviceLoginService_OTU service = RPC.callAsync(IDeviceLoginService_OTU.class);
            service.onLogIn(msg);*/
            log.info("收到登录事件,{}", msg);
            KV_Client kv = new KV_Client();
            kv.setIsOnline(true).setLastMsgTime(System.currentTimeMillis()).setLoginTime(System.currentTimeMillis()).saveOrUpdate(msg.getClientType(), msg.getEntityID());
            Eventbus.me().post(EventEnum.event_login, msg, TerminalContext.me().get(msg.getClientType(), msg.getEntityID()), kv);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 登出事件. 异步调用
     *
     * @param msg
     */
    @Override
    public void onLogOut(Message msg) {
        try {
            /*IDeviceLoginService_OTU service = RPC.callAsync(IDeviceLoginService_OTU.class);
            service.onLogOut(msg);*/
            try {
                log.info("收到登出事件,{}", msg);
                KV_Client kv = new KV_Client();
                kv.setIsOnline(false);
                List<byte[]> extend = msg.getExtend();
                KV_Terminal kv_terminal = TerminalContext.me().get(msg.getClientType(), msg.getEntityID());
                if (!CollectionUtil.isEmpty(extend)) {
                    Map<String, Boolean> map = SerializeUtil.parseByte(extend.get(0));
                    boolean isReplaced = map.get(CommunicationConfig.NETTY_SESSIONKEY_isReplaced);
                    if (!isReplaced) {

                        TerminalLoginDTO loginDTO = new TerminalLoginDTO();
                        loginDTO.setImei(kv_terminal.getImei());
                        RespModel<Boolean> loginOut = terminalService.loginOut(loginDTO);
                        if (Objects.nonNull(loginOut) && loginOut.getEntity()) {
                            log.info("设备IMEI--{}登出成功", kv_terminal.getImei());
                        } else {
                            log.info("设备IMEI--{}登出失败", kv_terminal.getImei());
                        }
                        Eventbus.me().post(EventEnum.event_logout, msg, TerminalContext.me().get(msg.getClientType(), msg.getEntityID()), kv);
                        kv.saveOrUpdate(msg.getClientType(), msg.getEntityID());
                    } else {
                        log.info("链接被踢:{}", msg);
                    }
                } else {
                    log.info("链接被踢:{}", msg);
                }

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Message getResponseMsg(Message msg) {
        if (OTUCodecUtil.isHeartMsg(msg)) {
            return new Message();
        }
        String functionStr = new String(msg.getFunction());
        byte[] respFunction = null;
        switch (functionStr) {
            case "1":
                respFunction = "2".getBytes();
                break;
            case "3":
                respFunction = "4".getBytes();
                break;
            case "7":
                respFunction = "8".getBytes();
                break;
            case "a3":
                respFunction = "a4".getBytes();
                break;
            case "A3":
                respFunction = "A4".getBytes();
                break;
            case "5":
                respFunction = "5".getBytes();

            default:
                break;
        }
        if (null == respFunction) {
            return null;
        }
        Message resp = new Message();
        resp.setFunction(respFunction);
        //登录消息,不用回复参数列表
        if (!isLoginMsg(msg)) {
            List<TV> tvList = new ArrayList<>();
            //只需要tag.不需要value
            for (TV tv : msg.getTvList()) {
                tvList.add(new TV(tv.getTag()));
            }
            resp.setTvList(tvList);
        }
        if("5".equals(functionStr)){
            resp = getTransResp(msg);
        }
        if (isTimeServiceMsg(msg)) {
            log.info("收到授时消息");
            resp.setClientType(ClientTypeEnum.OTU.getClientType());
            String content = DateTimeUtil.getCurrentUtcTime(DateTimeUtil.FormatInfo.DATE_yyMMddHHmmss);
            resp.setTvList(Arrays.asList(new TV("721".getBytes(), content.getBytes())));
        }
        return resp;
    }


    private void updateOnline(Message msg, boolean ttlStatus, int vehicleId) {
        try {

            if (ttlStatus) {
                KV_Client client = KV_Client.dao.find(msg.getClientType(), msg.getEntityID());
                long lastMsgTime = client.getLastMsgTime();
                long ttl = System.currentTimeMillis() - lastMsgTime;
                if (!(ttl > getTTL4RDBLastMsgTime() / 2)) {
                    return;
                }
            }
            new KV_Client().setIsOnline(true).saveOrUpdate(msg.getClientType(), msg.getEntityID());
            /*E_TbTerminal terminal = new E_TbTerminal();
            terminal.setLast_msg_time(new Date()).setId(Long.valueOf(msg.getEntityID())).setOnline(true).update();
            if (vehicleId != 0) {
                E_TbVehicle vehicle = new E_TbVehicle();
                vehicle.setId(Long.valueOf(vehicleId)).setOnline(true).update();
            }*/
        } catch (Exception e) {
            log.error("更新在线状态异常", e);
        }
    }

    private void updateOnline(Message msg, boolean ttlStatus) {
        try {
            KV_Terminal context = TerminalContext.me().get(msg.getClientType(), msg.getEntityID());
            updateOnline(msg, ttlStatus, context.getVehicle_id());
        } catch (Exception e) {
            log.error("更新在线状态异常", e);
        }
    }

    /**
     * @param msg
     * @Author: zhangxia
     * @Desc: 是否是授时服务请求
     * @MethodName: isTimeServiceMsg
     * @Date: 14:03 2019/5/24
     * @Return: java.lang.Boolean
     * @Version: 2.0
     */
    private Boolean isTimeServiceMsg(Message msg) {
        if (Objects.isNull(msg.getFunction()) || msg.getFunction().length <= 0) {
            return false;
        }
        if (Arrays.equals("1".getBytes(), msg.getFunction())) {
            List<TV> tvList = msg.getTvList();
            if (CollectionUtils.isEmpty(tvList) || tvList.size() <= 0) {
                return false;
            }
            TV tv = tvList.get(0);
            if (Arrays.equals("721".getBytes(), tv.getTag())) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }
}
