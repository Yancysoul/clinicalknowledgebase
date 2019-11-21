package com.ywhk.ckb.common.doc.core;

import com.alibaba.fastjson.JSONObject;
import com.ywhk.ckb.common.doc.domain.ParamInfo;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by zhurh on 17/6/30.
 */
public class ParamConverter {

    private static final String COOKIE_KEY = "API_DOC_";

    public String getData(HttpServletRequest request){

        Map<String,String> paramMap = getParamMap(request);

        Map<String,Object> jsonMap = new HashMap<>();
        for (Map.Entry<String,String> entry :paramMap.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            makeData(jsonMap,key,value);
        }
        handleListNull(jsonMap);
        return JSONObject.toJSONString(jsonMap);
    }


    private void handleListNull(Map<String,Object> jsonMap){
        for (Map.Entry<String,Object> entry :jsonMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof  List){
                List<Object> list = (List<Object>) value;
                Class<?> valueClass = null;
                if(!CollectionUtils.isEmpty(list)){
                    valueClass = list.get(0).getClass();
                }
                if(valueClass == null || valueClass == String.class || valueClass == Integer.class){
                    continue;
                }
                List<Object> newList = new ArrayList<>();
                for (Object object : list){
                    if(object == null){
                        continue;
                    }
                    newList.add(object);
                }
                jsonMap.put(key,newList);
            }
        }
    }

    private void makeData(Map<String,Object> jsonMap,String key,String value){
        if(!key.contains(".")){
            if(value.startsWith("[") && value.endsWith("]")  && value.indexOf(":") == -1){
                value = value.replace("\"","");
                Object[] valueItems = value.substring(1,value.length()-1).split(",");
                jsonMap.put(key, Arrays.asList(valueItems));
            }else{
                jsonMap.put(key,value);
            }
        }else{
            String currentKey = key.substring(0,key.indexOf("."));
            String subKey = key.substring(key.indexOf(".")+1,key.length());

            Integer currentIndex = null;
            if(currentKey.contains("[")){
                currentIndex = Integer.valueOf(currentKey.substring(currentKey.indexOf("[")+1,currentKey.indexOf("]")));
                currentKey = currentKey.substring(0,currentKey.indexOf("["));
            }

            Map<String,Object> map = null;
            if(currentIndex != null){
                List<Map<String,Object>> list = null;
                if(jsonMap.containsKey(currentKey)){
                    list = (List<Map<String, Object>>) jsonMap.get(currentKey);
                    if(list == null){
                        list = new ArrayList<>();
                        jsonMap.put(currentKey,list);
                    }
                }else{
                    list = new ArrayList<>();
                    jsonMap.put(currentKey,list);
                }

                if(list.size() <= currentIndex){
                    for (int i = list.size();i<currentIndex;i++){
                        list.add(null);
                    }
                    map = new HashMap<>();
                    list.add(map);
                }else{
                    map = list.get(currentIndex);
                    if(map == null){
                        map = new HashMap<>();
                        list.set(currentIndex,map);
                    }
                }
            }else{
                if(jsonMap.containsKey(currentKey)){
                    map = (Map<String, Object>) jsonMap.get(currentKey);
                }else{
                    map = new HashMap<>();
                    jsonMap.put(currentKey,map);
                }
            }
            makeData(map, subKey, value);
        }
    }

    private Map<String,String> getParamMap(HttpServletRequest request){
        //参数转换
        Map<String,String> paramMap = new LinkedHashMap<>();
        if(request.getParameterMap() != null && request.getParameterMap().size()>0){
            for (Map.Entry<String, String[]>  entry : request.getParameterMap().entrySet()){
                String key = entry.getKey();
                if("requestUrl".equals(key) || "currentService".equals(key) || "currentMethod".equals(key)){
                    continue;
                }
                String[] values = entry.getValue();
                String value = null;
                if(values != null && values.length > 0){
                    value = values[0];
                    if(StringUtils.isEmpty(value)){
                        continue;
                    }
                }
                paramMap.put(key,value);
            }
        }
        return  paramMap;
    }

    public String getCookieKey(String service,String method){
        return COOKIE_KEY+service+"_"+method;
    }

    public ParamInfo parseJsonToParam(HttpServletRequest request, String service, String method){
        ParamInfo paramInfo = new ParamInfo();
        String requestJson = request.getParameter("requestJson");
        if(StringUtils.isEmpty(requestJson)){
            //从cookie获取
            Cookie[] cookies = request.getCookies();
            if(cookies != null){
                for (Cookie cookie : cookies){
                    try {
                        if(cookie.getName().equals(getCookieKey(service,method))){
                            requestJson = URLDecoder.decode(cookie.getValue().toString(), "UTF-8");
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(StringUtils.isEmpty(requestJson)){
            return paramInfo;
        }
        requestJson = asciiToNative(requestJson);
        requestJson = requestJson.replace("\\","");
        try {
            Map<String,Object> jsonMap = JSONObject.parseObject(requestJson, Map.class);
            parse(paramInfo,jsonMap,"");
        }catch (Exception e){
            e.printStackTrace();
        }
        return paramInfo;
    }

    private void parse(ParamInfo paramInfo,Map<String,Object> jsonMap ,String name){
        String originalName = name;
        for (Map.Entry<String,Object> entry : jsonMap.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof Map){
                Map<String,Object> map = (Map<String, Object>) value;
                name = originalName+key+".";
                parse(paramInfo,map,name);
            }else if(value instanceof  List){
                List<Map<String,Object>> list = (List<Map<String, Object>>) value;
                if(!CollectionUtils.isEmpty(list)){
                    paramInfo.getListInfo().put(key,list.size());
                    if(paramInfo.getMaxListIndex() < list.size()){
                        paramInfo.setMaxListIndex(list.size());
                    }
                    for (int i=0;i<list.size();i++){
                        Map<String,Object> map = list.get(i);
                        name = originalName+key+"["+i+"].";
                        parse(paramInfo,map,name);
                    }
                }
            }else{
                if(value != null){
                    name = originalName+key;
                    paramInfo.getParams().put(name, value.toString());
                }
            }
        }
    }

    private String asciiToNative ( String asciicode ) {
        String[] asciis = asciicode.split ("\\\\u");
        String nativeValue = asciis[0];
        try {
            for ( int i = 1; i < asciis.length; i++ ) {
                String code = asciis[i];
                nativeValue += (char) Integer.parseInt (code.substring (0, 4), 16);
                if (code.length () > 4) {
                    nativeValue += code.substring (4, code.length ());
                }
            }
        }
        catch (NumberFormatException e)
        {
            return asciicode;
        }
        return nativeValue;
    }
}
