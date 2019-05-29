package com.demo.netty.protobuf.personinfo.handler;

import com.demo.netty.protobuf.personinfo.pojo.RichManProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ClientProtoHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        RichManProto.RichMan.Builder builder = RichManProto.RichMan.newBuilder();
        builder.setName("王思聪");
        builder.setId(1);
        builder.setEmail("wsc@163.com");

        List<RichManProto.RichMan.Car> cars = new ArrayList<RichManProto.RichMan.Car>();
        RichManProto.RichMan.Car car1 = RichManProto.RichMan.Car.newBuilder().setName("奥迪TT超跑").setBrand(RichManProto.RichMan.CarBrand.AUQI).build();
        RichManProto.RichMan.Car car2 = RichManProto.RichMan.Car.newBuilder().setName("Aventador").setBrand(RichManProto.RichMan.CarBrand.LAMBORGHINI).build();
        RichManProto.RichMan.Car car3 = RichManProto.RichMan.Car.newBuilder().setName("奔驰SLS级AMG").setBrand(RichManProto.RichMan.CarBrand.BENZ).build();

        cars.add(car1);
        cars.add(car2);
        cars.add(car3);

        builder.addAllCars(cars);
        ctx.writeAndFlush(builder.build());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("ClientProtoHandler消息处理发生异常：{}", cause.getStackTrace());
        ctx.close();
    }
}
