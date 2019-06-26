package com.demo.netty.idlecheck;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

import java.util.concurrent.TimeUnit;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/2/15
 * @time 13:48
 */
public class IdleHandler extends SimpleChannelInboundHandler<Message> {
    private int timeOut = 5;
    private int idleTime = 10;
    final static Timer timer = new HashedWheelTimer();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        final ChannelHandlerContext finalCtx = ctx;
        final IdleHandler handler = this;
        timer.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) {
                try {
                    if (finalCtx.channel().isActive()) {
                        //此处逻辑：tag为913，且两次请求间隔不超过10秒，index不为最大值
                        if (isUpdMsg(msg)) {
                            finalCtx.channel().pipeline().remove(handler);
                        } else {
                            finalCtx.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, idleTime, TimeUnit.SECONDS);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final ChannelHandlerContext finalCtx = ctx;
        final IdleHandler handler = this;
        timer.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) {
                try {
                    if (finalCtx.channel().isActive()) {
                        if (isLogined(finalCtx)) {
                            finalCtx.channel().pipeline().remove(handler);
                        } else {
                            finalCtx.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, timeOut, TimeUnit.SECONDS);
        ctx.fireChannelActive();
    }

    protected boolean isLogined(ChannelHandlerContext ctx) {
        return IdleSession.isLoginded;
    }

    private boolean isUpdMsg(Message msg){
        if("911".equals(msg.getTag())||"913".equals(msg.getTag())){
            return true;
        }
        return false;
    }
}
