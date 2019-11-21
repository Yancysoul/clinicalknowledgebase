package com.ywhk.ckb.common.util;

import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuronghui
 * @date 2019年08月18日
 * @description 对象工具类
 */
public final class ObjectUtils {

    /**
     * JPA自定义sql查询返回自定义对象转换方法
     * @param list
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    @SneakyThrows
    public static <T> List<T> castEntity(List<Object[]> list, Class<T> clazz) {
        List<T> returnList = new ArrayList<>();
        if (list.size() == 0){
            return returnList;
        }
        Class[] c2 = null;
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors){
            Class[] tClass = constructor.getParameterTypes();
            if (tClass.length == list.get(0).length){
                c2 = tClass;
                break;
            }
        }
        //构造方法实例化对象
        for(Object[] o : list){
            Constructor<T> constructor = clazz.getConstructor(c2);
            returnList.add(constructor.newInstance(o));
        }
        return returnList;
    }

}
