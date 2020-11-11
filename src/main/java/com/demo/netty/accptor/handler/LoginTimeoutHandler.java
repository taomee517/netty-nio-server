//package com.demo.netty.accptor.handler;
//
//import com.blackTea.util.log.LogUtil;
//import com.demo.netty.accptor.session.NettySession;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
//import io.netty.util.HashedWheelTimer;
//import io.netty.util.Timeout;
//import io.netty.util.Timer;
//import io.netty.util.TimerTask;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//
//import java.util.concurrent.TimeUnit;
//
//@Slf4j
//public class LoginTimeoutHandler extends ChannelInboundHandlerAdapter {
////    static Logger logger = LogUtil.getLogger(LoginTimeoutHandler.class);
//
//    private int timeOut = 5;
//
//    final static Timer timer = new HashedWheelTimer();
//
//    public LoginTimeoutHandler() {
//    }
//
//    public LoginTimeoutHandler(int timeOut) {
//        super();
//        this.timeOut = timeOut;
//    }
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        final ChannelHandlerContext finalCtx = ctx;
//        final LoginTimeoutHandler handler = this;
//        timer.newTimeout(new TimerTask() {
//            public void run(Timeout timeout) {
//                try {
//                    if (finalCtx.channel().isActive()) {
//                        if (isLogined(finalCtx)) {
//                            finalCtx.channel().pipeline().remove(handler);
//                        } else {
//                            log.info("检查登陆超时,通道即将关闭{}", finalCtx.channel());
//                            finalCtx.close();
//                        }
//                    }
//                } catch (Exception e) {
//                    log.error(e.getMessage(), e);
//                }
//            }
//        }, timeOut, TimeUnit.SECONDS);
//        ctx.fireChannelActive();
//    }
//
//    protected boolean isLogined(ChannelHandlerContext ctx) {
//        return NettySession.isLoginIn(ctx.channel());
//    }
//}
