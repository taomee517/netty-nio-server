package com.demo.netty.protobuf.personinfo.handler;

import com.demo.netty.protobuf.personinfo.pojo.RichManProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ServerProtoHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RichManProto.RichMan man = ((RichManProto.RichMan) msg);
        log.info("{}共有{}辆车：", man.getName(), man.getCarsCount());
        List<RichManProto.RichMan.Car> cars = man.getCarsList();
        for(RichManProto.RichMan.Car car : cars){
            log.info(car.getBrand() + " ： " + car.getName());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("ProtoHandler消息处理发生异常：{}", cause.getStackTrace());
        ctx.close();
    }
}
