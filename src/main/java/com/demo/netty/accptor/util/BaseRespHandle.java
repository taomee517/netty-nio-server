///**
// *
// */
//package com.demo.netty.accptor.util;
//
//import com.blackTea.common.model.Message;
//import com.blackTea.common.model.TV;
//import com.blackTea.util.log.LogUtil;
//import com.demo.netty.accptor.constant.GL500Constants;
//import com.demo.netty.accptor.session.NettySession;
//import io.netty.channel.ChannelHandlerContext;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author jgchen
// */
//public abstract class BaseRespHandle {
//
//    private static org.slf4j.Logger logger = LogUtil.getLogger(BaseRespHandle.class);
//
//    /**
//     * 这个是设备主动上报的
//     */
//    public void handleResp(ChannelHandlerContext ctx, List<Object> out, String upMsg) {
//        //去掉头
//        upMsg = upMsg.replace(getUpMsgPrefix(), "");
//        //去掉尾
//        upMsg = upMsg.replace(String.valueOf((char)GL500Constants.END_SIGN), "");
//
//        String[] upMsgArr = upMsg.split(",");
//
//        String tag = upMsgArr[0];
//        //+1，是逗号
//        String content = upMsg.substring(tag.length() + 1);
//
//        Message message = new Message();
//        //用于返回那边的encode的处理
//        message.setFunction((GL500Constants.RESP_PREFIX + tag).getBytes());
//        //GT是前缀，可去掉，后面的都是表示特定意义的
//        String subTag = tag.replace(GL500Constants.TAG_PREFIX, "");
//        TV tv = new TV(subTag.getBytes(), content.getBytes());
//
//        List<TV> tvList = new ArrayList<>();
//        tvList.add(tv);
//
//        message.setTvList(tvList);
//        out.add(message);
//
//        //判定是否登录
//        handleLogin(ctx, out, content, message);
//
//    }
//
//    private void handleLogin(ChannelHandlerContext ctx, List<Object> out, String content, Message message) {
//        boolean isLogin = NettySession.isLoginIn(ctx.channel());
//        //没有登录过
//        if (!isLogin) {
//            //必须要设置，产生登录业务，在NettyServerMsghandler上处理
//            message.setFunction(GL500Constants.LOGIN_FUNCTION.getBytes());
//            //			message.addTv(new TV(GL500Constants.LOGIN_FUNCTION.getBytes(), content.getBytes()));//此处的内容，只是需要imei
//
//            addLoginTask(out, content);
//        }
//    }
//
//    private void addLoginTask(List<Object> out, String content) {
//        //下发3次查询GPS
//        Message message = new Message();
//        //用于返回那边的encode的处理
//        message.setFunction((GL500Constants.LOGIN_TASK_FUNCTION).getBytes());
//        //GT是前缀，可去掉，后面的都是表示特定意义的
//        String subTag = GL500Constants.LOGIN_TASK_FUNCTION.replace(GL500Constants.TAG_PREFIX, "");
//
//        TV tv = new TV(subTag.getBytes(), content.getBytes());
//        message.addTv(tv);
//
//        out.add(message);
//    }
//
//    public abstract String getUpMsgPrefix();
//
//}
