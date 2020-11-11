///**
// *
// */
//package com.demo.netty.accptor.service;
//
//import com.blackTea.common.constants.ClientTypeEnum;
//import com.blackTea.common.model.Message;
//
///**
// * 设备的身份相关, 如登录,是否是登录消息, 心跳超时时间等.
// *
// * @author yu.hou, email: houyujiangjun@qq.com
// * @date 2017年12月4日
// */
//public interface IDeviceConfigService {
//    /**
//     * 是否是寻址消息
//     *
//     * @param msg
//     * @return
//     */
//    public boolean isAddressMsg(Message msg);
//
//    /**
//     * 处理寻址消息
//     *
//     * @param msg
//     * @return
//     */
//    public Message handleAddress(Message msg);
//
//    /**
//     * 当前消息是否是登录消息
//     *
//     * @param msg
//     * @return
//     */
//    public boolean isLoginMsg(Message msg);
//
//    /**
//     * 设备登录,
//     *
//     * @param msg
//     * @return 返回null意味着登录失败, 登录成功, 返回值中应携带clienttype和entityid
//     */
//    public Message login(Message msg);
//
//    /**
//     * 有的时候 离线统计是很重要的功能, 因此 设备会在心跳超过 一段时期后主动更新数据库.
//     * 但这个时间需要单独配置, 有时候统计不需要实时, 降低频率可以降低对关系型数据库update时的性能冲击
//     *
//     * @return 心跳超时时间, 单位 毫秒, 如果为0, 代表不开启
//     */
//    public long getTTL4RDBLastMsgTime();
//
//    /**
//     * 各自实现更新关系型数据库的最后消息时间
//     *
//     * @param ClientType
//     * @param entityID
//     */
//    public void updateRDBLastMsgTime(int ClientType, int entityID);
//
//    /**
//     * ttl 和其它一些重要消息是在ClientTypeEnum中配置的
//     *
//     * @return
//     */
//    public ClientTypeEnum getClientTypeEnum();
//
//    /**
//     * 各种协议可能有千奇百怪的响应策略和原则,但很多消息需要在接入端直接回复,来提升系统响应.<br/>
//     * 所以各接入类型自行实现响应消息的生成
//     *
//     * @param msg
//     * @return 如果返回null, 就是不能直接回复
//     */
//    public Message getResponseMsg(Message msg);
//
//    /**
//     * 登录成功事件, 异步调用
//     *
//     * @param msg
//     */
//    public void onLogIn(Message msg);
//
//    /**
//     * 登出事件. 异步调用
//     *
//     * @param msg
//     */
//    public void onLogOut(Message msg);
//
//}
