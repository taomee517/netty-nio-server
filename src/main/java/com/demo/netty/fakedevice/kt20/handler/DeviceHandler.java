package com.demo.netty.fakedevice.kt20.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 模拟聊天室客户端消息处理类
 *
 * @Author luotao
 * @E-mail taomee517@qq.com
 * @Date 2019\1\27 0027 16:47
 */
@Slf4j
public class DeviceHandler extends ChannelInboundHandlerAdapter {
    private int index;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String serverMsg = msg.toString();
        log.info("服务器消息: {}", serverMsg);
        if (index<1) {
            //3,7#b401,1,2
//            String up = "7E0900000D014533224352000441332C3723623430312C312C325A7E";
            //3,1#b709,
//            String up = "7E0900000A014533224352000441332C3123623730392C7F7E";
            //鉴权消息
//            String up = "7E010200140145332243520002465A4B2D42534A2D4B5432302D434F4E4649524D3F7E";
            //定位消息
//            String up = "7E0200002201453322435200030000000000000001015F1FDA06D0AA40000000000000190812195834010400000000B27E";
            String temp = "7E 02 00 00 3C 01 45 33 22 43 52 0B 1B 00 00 00 00 80 12 40 02 01 C4 11 D0 06 58 D8 90 02 32 00 00 00 C8 19 09 10 12 58 38 01 04 00 00 00 07 02 08 00 00 00 00 00 00 00 00 BC 0E 00 0C 00 B2 89 86 04 41 19 18 C3 61 50 10 8F 7E";
            String up = temp.replaceAll(" ", "");
            //心跳消息
//            String up = "7E000200000145332243520004427E";
            //登出消息
//            String up = "7E000300000145332243520004437E";
            //查询结果
//            String up = "7E01040188013620125135032300002B0000000104000000B40000000204000000000000000304000000000000000404000000000000000504000000000000000604000000000000000704000000000000001005434D4E455400000011000000001200000000130E3132332E36352E3231362E323436000000140000000015000000001600000000170E3132332E36352E3231362E32343600000018040000226600000019040000000000000020040000000000000021040000000000000022040000000000000027040000007800000028040000000000000029040000001E0000002C040000000000000050040000000000000052040000000000000053040000000000000055040000007800000056040000000A00000057040000000000000058040000000000000059040000003C0000005A04000000000000007004000000000000007104000000000000007204000000000000007304000000000000007404000000000000008004000000000000008102002C0000008202012F000000830CD4C142383838383820202020000000840101897E";
            log.info("模拟设备消息: {}", up);
            ctx.channel().writeAndFlush(up);
            index++;
        }

        //查询位置
        if(serverMsg.contains("8201")){
            String location = "7E0201003E01453322435200487256000000008012400201C411D00658D8900232000000C819091012583801040000000702080000000000000000BC0E000C00B2898604411918C3615010F07E";
//            String location = "7E0201002401453322435200487256000000000000000101C4D08C06583900000000000000190826182715010400000000E87E";
            log.info("回复位置：{}", location);
            ctx.channel().writeAndFlush(location);
            //查询能力
        }else if(serverMsg.contains("62313032")){
            String ability = "7E0900001E014533224352000141362C3223623130322C3031313130313131313030303030303030303023777E";
            log.info("回复能力：{}", ability);
            ctx.channel().writeAndFlush(ability);
            //控制
        }else if(serverMsg.contains("62353031")){
            //目前写死，只回复解锁结果
            String result = "7E0900000E014533224352037B41362C3723623430312C322C3223007E";
            log.info("回复控制结果：{}", result);
            ctx.channel().writeAndFlush(result);
        }else if(serverMsg.contains("62323230")){
            log.info("蓝牙清除指令: {}", serverMsg );
            String clearBtCfgResp = "7E09000009014533224352698F41362C342362323230BC7E";
            log.info("回复清除蓝牙配置：{}", clearBtCfgResp);
            ctx.channel().writeAndFlush(clearBtCfgResp);
        }else if(serverMsg.contains("62323033")){
            log.info("蓝牙设置指令: {}", serverMsg );
            String setBtCfgResp = "7E09000014014533224352C2EC41362C3423623230332C324E393253322C316364487E";
            log.info("回复蓝牙配置结果：{}", setBtCfgResp);
            ctx.channel().writeAndFlush(setBtCfgResp);
            log.info("蓝牙设置成功! ");
        }else if(serverMsg.contains("62313031")){
            log.info("查询版本指令: {}", serverMsg );
            String versionResp = "7E0900005A014533224352007741362C3723623130312C6B65796C73735F6D2E626173652E313035622C323131322E64656275672E312C44595F56322E30302E3030312C76775F71335F323031372E34302C312C312C465A4B2D45374245414132363833413323507E";
            log.info("回复版本信息：{}", versionResp);
            ctx.channel().writeAndFlush(versionResp);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent)evt;
        if(event.equals(IdleStateEvent.WRITER_IDLE_STATE_EVENT)){
            String heatbeat = "7E000200000145332243520004427E";
            log.info("心跳消息: {}", heatbeat);
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
