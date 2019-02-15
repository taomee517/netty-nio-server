package com.demo.netty.idlecheck;

import java.util.function.Predicate;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/2/15
 * @time 13:27
 */
public class Message {
    private boolean isHb;
    private String head;
    private String tag;
    private int index;

    public boolean isHb() {
        return isHb;
    }

    public void setHb(boolean hb) {
        isHb = hb;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
