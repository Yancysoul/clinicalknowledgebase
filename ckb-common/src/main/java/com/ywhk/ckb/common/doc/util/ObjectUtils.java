package com.ywhk.ckb.common.doc.util;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ObjectUtils {
    @SneakyThrows
    public static Map<String,Object> objectToMap(Object object, boolean withParentFields){

        if(object == null){
            return Collections.emptyMap();
        }

        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        Map<String,Object> map = new HashMap<>();
        for (Field field : fields){
            field.setAccessible(true);
            map.put(field.getName(),field.get(object));
        }

        if(withParentFields){
            fields = clazz.getSuperclass().getDeclaredFields();
            for (Field field : fields){
                field.setAccessible(true);
                map.put(field.getName(),field.get(object));
            }
        }

        return map;
    }

    @SneakyThrows
    public static Map<String,Object> objectToMap(Object object){
        return objectToMap(object,false);
    }

    public static String objectToFormParam(Object object){
        Map<String,Object> paramMap = objectToMap(object,false);
        StringBuilder stringBuilder= new StringBuilder();
        for (Map.Entry<String,Object> entry : paramMap.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value == null){
                continue;
            }
            stringBuilder.append(key+"="+value.toString()).append("&");
        }
        String paramStr = stringBuilder.toString();
        if(paramStr.endsWith("&")){
            paramStr = paramStr.substring(0,paramStr.length()-1);
        }
        return paramStr;
    }
}
