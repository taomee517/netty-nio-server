package com.demo.netty.fakedevice.kt20.util;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ListUtil {
	public static boolean isNull(String[] paras) {
		boolean b = true;
		for(int i=0;i<paras.length;i++) {
			if(!StringUtils.isBlank(paras[i])) {
				b = false;
				return b;
			}
		}
		return b;
		
	}
	//判断是否所有的参数都是空
	public static boolean isNull(List<String> paras) {
		boolean b = true;
		for(int i=0;i<paras.size();i++) {
			if(!StringUtils.isBlank(paras.get(i))) {
				b = false;
				return b;
			}
		}
		return b;

	}
	//判断是否有某个参数为空
	public static boolean anyOneIsNull(List<String> paras) {
		boolean b = false;
		for(int i=0;i<paras.size();i++) {
			if(StringUtils.isBlank(paras.get(i))) {
				b = true;
				return b;
			}
		}
		return b;

	}
}
