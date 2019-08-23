package com.demo.netty.fakedevice.kt20.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
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
        log.info("收到服务端回复：{}", msg.toString());
        if (index<1) {
            //3,7#b401,1,2
//            String up = "7E0900000D014533224352000441332C3723623430312C312C325A7E";
            //3,1#b709,
//            String up = "7E0900000A014533224352000441332C3123623730392C7F7E";
            //鉴权消息
//            String up = "7E010200140145332243520002465A4B2D42534A2D4B5432302D434F4E4649524D3F7E";
            //定位消息
//            String up = "7E0200002201453322435200030000000000000001015F1FDA06D0AA40000000000000190812195834010400000000B27E";
            //心跳消息
//            String up = "7E000200000145332243520004427E";
            //登出消息
            String up = "7E000300000145332243520004437E";
            //查询结果
//            String up = "7E01040188013620125135032300002B0000000104000000B40000000204000000000000000304000000000000000404000000000000000504000000000000000604000000000000000704000000000000001005434D4E455400000011000000001200000000130E3132332E36352E3231362E323436000000140000000015000000001600000000170E3132332E36352E3231362E32343600000018040000226600000019040000000000000020040000000000000021040000000000000022040000000000000027040000007800000028040000000000000029040000001E0000002C040000000000000050040000000000000052040000000000000053040000000000000055040000007800000056040000000A00000057040000000000000058040000000000000059040000003C0000005A04000000000000007004000000000000007104000000000000007204000000000000007304000000000000007404000000000000008004000000000000008102002C0000008202012F000000830CD4C142383838383820202020000000840101897E";
            log.info("模拟设备消息: {}", up);
            ctx.channel().writeAndFlush(up);
            index++;
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String in = "7E010000210145332243520001002C012F37303131314B542D32302020206342440257666501D4C14238383838381C7E";
        log.info("模拟设备注册消息: {}", in);
        ctx.channel().writeAndFlush(in);
//        InputStream input = System.in;
//        BufferedReader br = new BufferedReader(new InputStreamReader(input));
//        while (true){
//            ctx.channel().writeAndFlush(br.readLine());
//        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("DeviceHandler发生异常：", cause);
        ctx.close();
    }
}
