package com.ywhk.ckb.common.util;

import com.squareup.okhttp.*;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zhurh
 */
@Slf4j
public class HttpUtils {
    public static String postWithJsonBody(String url,String jsonParams,Map<String,String> headerMap) throws Exception{
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonParams);

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(100L, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(120L, TimeUnit.SECONDS);
        Request.Builder builder = new Request.Builder();
        builder.url(url).post(body);
        if(!CollectionUtils.isEmpty(headerMap)){
            for (Map.Entry<String,String> entry : headerMap.entrySet()){
                builder.addHeader(entry.getKey(),entry.getValue());
            }
        }
        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        return getResponse(call,url);
    }

    public static String postWithJsonBody(String url,String jsonParams) throws Exception{
        return postWithJsonBody(url,jsonParams, Collections.emptyMap());
    }

    public static String get(String url,Map<String,String> headerMap) throws Exception{
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if(!CollectionUtils.isEmpty(headerMap)){
            for (Map.Entry<String,String> entry : headerMap.entrySet()){
                builder.addHeader(entry.getKey(),entry.getValue());
            }
        }
        builder.get();
        Request request = builder.build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        return getResponse(call,url);
    }

    private static String getResponse(Call call,String url) throws Exception{
        try {
            Response response = call.execute();
            if(response.code() != 200){
                throw new RuntimeException("请求"+url+"返回错误的响应码，returnCode:"+response.code());
            }
            @Cleanup ResponseBody responseBody = response.body();
            return responseBody.string();
        } catch (IOException e) {
            log.error("请求"+url+"失败，error:"+e.getMessage(),e);
            throw e;
        }
    }
}
