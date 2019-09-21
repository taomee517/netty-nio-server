package com.demo.netty.fakedevice.kt20.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

public class ReflectUtil {

    public static int getNotNullFieldSize(Object obj) throws Exception{
        int count = 0;
        Field[]  fields = obj.getClass().getDeclaredFields();
        for(Field field : fields){
            String fieldName = field.getName();
            String methodName = "get" + fieldName.replaceFirst(fieldName.substring(0,1),fieldName.substring(0,1).toUpperCase());
            Method method = obj.getClass().getMethod(methodName,null);
            Object fieldValue = method.invoke(obj,null);
            if(!Objects.isNull(fieldValue)){
                count ++;
            }
        }
        return count;
    }

}
