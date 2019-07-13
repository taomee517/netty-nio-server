package com.demo.netty.component.attributemap;

import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class AttrDemo {
    public static final  AttributeKey<String> IMEI = AttributeKey.newInstance("imei");
    public static final  AttributeKey<String> PROD = AttributeKey.newInstance("product");

    public static void main(String[] args) {
        NioServerSocketChannel channel = new NioServerSocketChannel();
        channel.attr(IMEI).set("866323033587907");
        Attribute imeiAttr = channel.attr(IMEI);

        channel.attr(PROD).set("otu");
        Attribute prodAttr = channel.attr(PROD);
        System.out.println(imeiAttr.get());
        System.out.println(prodAttr.get());
    }
}
