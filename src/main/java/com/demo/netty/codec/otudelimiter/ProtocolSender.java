package com.demo.netty.codec.otudelimiter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;

@Slf4j
public class ProtocolSender extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        String msg = DefaultValue.DEFAULT_FIRST_MSG;
//        ByteBuf buf = Unpooled.buffer(msg.length());
//        for(int i=0;i<msg.length();i++){
//            buf.writeByte((byte)msg.charAt(i));
//        }
//        ctx.writeAndFlush(buf);
//        System.out.println("向服务器发送消息：" + msg);

        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File("E:\\private\\test\\terminal_logs\\otu\\acceptor1.xlsx")));
        XSSFSheet sheet = null;
        String in = null;
        int count = 0;
        // 获取每个Sheet表
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheet = workbook.getSheetAt(i);
            // getLastRowNum，获取最后一行的行标
            for (int j = 0; j < sheet.getLastRowNum() + 1; j++) {
                count = j;
                XSSFRow row = sheet.getRow(j);
                if (row != null) {
                    // getCell 获取单元格数据
                    if (row.getCell(0) != null) {
                        XSSFCell protocol = row.getCell(0);
                        String content = protocol.toString();
                        if (StringUtils.isNotEmpty(content)) {
                            if (content.startsWith("6816")) {
                                String msg = content.split(",")[6];
                                msg = msg.replace("★",",");
                                in = "(1" + msg + ")";
                            }else {
                                in = "(1" + content + ")";
                            }
                        }
                        log.info(in);
                        ByteBuf buf = Unpooled.copiedBuffer(in, Charset.forName("UTF-8"));
                        ctx.writeAndFlush(buf);
                    }
                }
            }
            log.info("读取sheet表：" + workbook.getSheetName(i) + " 完成,读到第"+ (count+1) + "行");
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        byte[] respArr = new byte[buf.readableBytes()];
        buf.readBytes(respArr);
        String text = new String(respArr, "UTF-8");
        log.info("收到服务器消息：{}", text);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("客户端消息发送时发生异常：{}", cause);
        ctx.close();
    }
}
