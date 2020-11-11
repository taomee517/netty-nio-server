///**
// *
// */
//package com.demo.netty.accptor.service;
//
//import com.blackTea.common.model.Message;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//
//import java.util.List;
//
///**
// * 各种协议需要自行实现从 字节流到 nettyMsg的转换. 使用spi来发现服务
// *
// * @author yu.hou, email: houyujiangjun@qq.com
// * @date 2017年11月28日
// */
//public interface IByteToNettyMsgCodecService {
//
//    /**
//     * 移动指针到开始位置
//     *
//     * @param ctx
//     * @param in
//     */
//    public default void resetBuffer(ChannelHandlerContext ctx, ByteBuf in) {
//        int left = in.readableBytes();
//        int start = in.readerIndex();
//        if (left > 0 && in.readerIndex() > 0) {
//            for (int index = 0; index < left; index++) {
//                in.setByte(index, in.getByte(index + start));
//            }
//            in.setIndex(0, left);
//        }
//    }
//
//    /**
//     * 编码
//     *
//     * @param ctx
//     * @param msg
//     * @param out
//     * @return 返回String类型的数据, 用于打印日志
//     * @throws Exception
//     */
//    public String encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception;
//
//    /**
//     * 解码
//     *
//     * @param ctx
//     * @param in
//     * @param out
//     * @return 返回String类型的数据, 用于打印日志
//     * @throws Exception
//     */
//    public String decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception;
//}
