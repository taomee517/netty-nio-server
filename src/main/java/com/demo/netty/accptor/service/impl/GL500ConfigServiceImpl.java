/**
 *
 */
package com.demo.netty.accptor.service.impl;


import com.blackTea.common.communicate.netty.config.CommunicationConfig;
import com.blackTea.common.constants.ClientTypeEnum;
import com.blackTea.common.eventbus.EventEnum;
import com.blackTea.common.eventbus.Eventbus;
import com.blackTea.common.model.Message;
import com.blackTea.common.model.TV;
import com.blackTea.common.model.TerminalContext;
import com.blackTea.util.StrUtil;
import com.blackTea.util.log.LogUtil;
import com.blackTea.util.serialize.SerializeUtil;
import com.demo.netty.accptor.constant.GL500Constants;
import com.demo.netty.accptor.constant.MSClientEnum;
import com.demo.netty.accptor.service.IDeviceConfigService;
import com.demo.netty.accptor.service.ITerminalService;
import com.demo.netty.accptor.util.GL500CodecUtil;
import com.fzk.otu.utils.CollectionUtil;
import com.fzk.otu.utils.StringUtils;
import com.fzk.terminal.api.dto.TerminalLoginDTO;
import com.fzk.terminal.api.vo.TerminalVO;
import com.mysirui.springcloud.SpringCloudSdk;
import com.mysirui.springcloud.api.vo.RespModel;
import kv.m.KV_Client;
import kv.m.KV_Terminal;

import java.util.*;

import static com.demo.netty.accptor.constant.GL500Constants.UNKOWN_SOFTVERSION;

/**
 * @author yu.hou, email: houyujiangjun@qq.com
 * @date 2017年12月4日
 */
public class GL500ConfigServiceImpl implements IDeviceConfigService {
    private static org.slf4j.Logger logger = LogUtil.getLogger(GL500ConfigServiceImpl.class);
    private static ITerminalService terminalService = (ITerminalService) SpringCloudSdk.connect(ITerminalService.class, MSClientEnum.MS_TERMINAL_CLIENT.getMsName());

    /**
     * 是否是寻址消息
     * GL500 是没有寻址的
     *
     * @param msg
     * @return
     */
    @Override
    public boolean isAddressMsg(Message msg) {
        return false;
    }

    /**
     * 处理寻址消息
     *
     * @param msg
     * @return
     */
    @Override
    public Message handleAddress(Message msg) {
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
        String loginFunction = new String(msg.getFunction());
        if (GL500Constants.LOGIN_FUNCTION.equals(loginFunction)) {
            return true;
        }
        return false;
    }

    /**
     * 设备登录,
     *
     * @param msg
     * @return 返回null意味着登录失败, 登录成功, 返回值中应携带clienttype和entityid
     */
    @Override
    public Message login(Message msg) {
        String value = new String(msg.getTvList().get(0).getValue());

        String imei = value.split(",")[1];
        if (StrUtil.isBlank(imei)) {
            logger.error("imei不存在");
            return null;
        }
        logger.info("GL500设备登陆请求,imei:{}", imei);
        TerminalLoginDTO dto = new TerminalLoginDTO();
        dto.setImei(imei);
        dto.setSoftVersion(UNKOWN_SOFTVERSION);
        RespModel<TerminalVO> login = terminalService.login(dto);
        if (login == null || login.getEntity() == null) {
            logger.info("gl500登录失败");
            return null;
        }
        TerminalVO tv = login.getEntity();

        Message result = msg.clone();
        //将设备的imei设置到message中
        result.addTv(new TV("101".getBytes(), imei.getBytes()));
        result.setClientType(ClientTypeEnum.GL500.getClientType());
        //TODO 暂时先不验证.直接存到数据库.
        result.setEntityID(Integer.parseInt(String.valueOf(tv.getId())));
        result.setLevelcode(tv.getLevelCode());
        return result;
    }

    /**
     * 有的时候 离线统计是很重要的功能, 因此 设备会在心跳超过 一段时期后主动更新数据库.
     * 但这个时间需要单独配置, 有时候统计不需要实时, 降低频率可以降低对关系型数据库update时的性能冲击
     *
     * @return 心跳超时时间, 单位 毫秒, 如果为0, 代表不开启
     */
    @Override
    public long getTTL4RDBLastMsgTime() {
        return 1 * 60 * 1000L;//一分钟一次 更新数据库中lastmsgtime字段
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
            logger.error("kv_terminal不存在");
            return;
        }
        try {
            String imei = ter.getImei();
            if(StringUtils.isEmpty(imei)){
                imei = terminalService.getInfo(entityID).getEntity().getImei();
            }
            RespModel<Boolean> booleanRespModel = terminalService.updateTerminalLastMsg(imei);
            if (Objects.nonNull(booleanRespModel)) {
                logger.info("设备id-{}-更新lastmsg结果--{}", ter.getEntityid(), booleanRespModel.getEntity());
            }
        } catch (Exception e) {
            logger.error("更新lastmsg异常--{}", e);
        }
    }

    /**
     * ttl 和其它一些重要消息是在ClientTypeEnum中配置的
     *
     * @return
     */
    @Override
    public ClientTypeEnum getClientTypeEnum() {
        return ClientTypeEnum.GL500;
    }

    /**
     * 登录成功事件, 异步调用
     *
     * @param msg
     */
    @Override
    public void onLogIn(Message msg) {
        try {
            logger.info("收到登录事件,{}", msg);
            KV_Client kv = new KV_Client();
            kv.setIsOnline(true).setLastMsgTime(System.currentTimeMillis()).setLoginTime(System.currentTimeMillis()).saveOrUpdate(msg.getClientType(), msg.getEntityID());
            Eventbus.me().post(EventEnum.event_login, msg, TerminalContext.me().get(msg.getClientType(), msg.getEntityID()), kv);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
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
            logger.info("收到登出事件,{}", msg);
            KV_Terminal kv_terminal = TerminalContext.me().get(msg.getClientType(), msg.getEntityID());
            TerminalLoginDTO dto = new TerminalLoginDTO();
            dto.setImei(kv_terminal.getImei());
            dto.setSoftVersion(UNKOWN_SOFTVERSION);
            RespModel<Boolean> loginOut = terminalService.loginOut(dto);
            if (loginOut == null || loginOut.getEntity() == null) {
                logger.info("gl500登出失败");
                return;
            }
            KV_Client kv = new KV_Client();
            kv.setIsOnline(false);
            List<byte[]> extend = msg.getExtend();
            if (!CollectionUtil.isEmpty(extend)) {
                Map<String, Boolean> map = SerializeUtil.parseByte(extend.get(0));
                boolean isReplaced = map.get(CommunicationConfig.NETTY_SESSIONKEY_isReplaced);
                if (!isReplaced) {
                    kv.saveOrUpdate(msg.getClientType(), msg.getEntityID());
                } else {
                    logger.info("链接被踢:{}", msg);
                }
            }else {
                logger.info("链接被踢:{}", msg);
            }
            Eventbus.me().post(EventEnum.event_logout, msg, TerminalContext.me().get(msg.getClientType(), msg.getEntityID()), kv);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public Message getResponseMsg(Message msg) {
        if (GL500CodecUtil.isHeartMsg(msg)) {
            return new Message();
        }
        String functionStr = new String(msg.getFunction());
        byte[] respFunction = null;

        //		if (functionStr.startsWith("+RESP:")) {
        //			respFunction = "+SACK:".getBytes();
        //		} else if (functionStr.startsWith("+ACK:GTHBD")) {
        //			//心跳
        //
        //		}

        if (null == respFunction) {
            return null;
        }

        Message resp = new Message();
        resp.setFunction(respFunction);
        List<TV> tvList = new ArrayList<>();
        //只需要tag.不需要value
        for (TV tv : msg.getTvList()) {
            tvList.add(new TV(tv.getTag()));
        }
        resp.setTvList(tvList);

        return resp;
    }
}
