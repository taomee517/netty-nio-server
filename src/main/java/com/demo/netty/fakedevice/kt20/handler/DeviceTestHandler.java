package com.demo.netty.fakedevice.kt20.handler;

import com.demo.netty.accptor.util.BytesTranUtil;
import com.demo.netty.fakedevice.kt20.util.BytesUtil;
import com.demo.netty.fakedevice.kt20.util.MessageBuilder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeviceTestHandler extends ChannelInboundHandlerAdapter {
    //7E090008600145332243520006FF4607F2513252077F6AF7D97054A7E1D0D0C53F08BF6950481DBF04CEF9917F1A594203AD6300ED5546AA122FBB340B914681E0398EDA1957DD91F4A09BCD2B0BE7EC436B32677D02292F05F326319060EEC478E8008B47F172BA8EFE0D999A63D47E

    private int index = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String serverMsg = ((String) msg);
        log.info("服务器消息：{}", serverMsg);
        if (index == 0) {
            for (int i = 0; i<50; i++) {
                Thread.sleep(50);
                if(i % 5 == 0){
                    ctx.channel().writeAndFlush("7E7E");
                }else {
                    String statusContent = "41362C3523623330312C303030302C31313131312C313030302C30303030302C30302C32302C302C2C2C2C3830303030302C2C3463352C3133322C2C2C302C383030302C4F2C2C383030302C383030302C2C653823";
                    String snNo = "14533224352";
                    String srcMsgId = "0900";
                    byte[] content = BytesTranUtil.hexStringToBytes(statusContent);
                    boolean aesEncode = true;
                    String upMsg = MessageBuilder.buildMsg(snNo,srcMsgId,content,i,aesEncode);
                    log.info("生成消息：clientMsg = {}", upMsg);
                    ctx.channel().writeAndFlush(upMsg);
                }
            }
            index ++;
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent)evt;
        if(event.equals(IdleStateEvent.WRITER_IDLE_STATE_EVENT)){
            String heatbeat = "7E000200000145332243520004427E";
            log.info("C ——> S");
            ctx.channel().writeAndFlush(heatbeat);
        }
    }



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String in = "7E010000210145332243520001002C012F37303131314B542D32302020206342440257666501D4C14238383838381C7E";
        log.info("模拟设备注册消息: {}", in);
        ctx.channel().writeAndFlush(in);

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("DeviceHandler发生异常：", cause);
        ctx.close();
    }
}
