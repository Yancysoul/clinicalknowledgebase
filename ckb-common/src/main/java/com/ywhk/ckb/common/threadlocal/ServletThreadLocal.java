package com.ywhk.ckb.common.threadlocal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletThreadLocal {
    private static ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<String> userIdThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<String> adminUserIdThreadLocal = new ThreadLocal<>();

    /**
     * 获取HttpServletRequest
     * @return
     */
    public static  HttpServletRequest getRequest(){
        return requestThreadLocal.get();
    }

    /**
     * 获取HttpServletResponse
     * @return
     */
    public static  HttpServletResponse getResponse(){
        return responseThreadLocal.get();
    }

    /**
     * 设置HttpServletRequest
     * @param request
     */
    public static  void putRequest(HttpServletRequest request){
        requestThreadLocal.set(request);
    }

    /**
     * 设置HttpServletResponse
     * @param response
     */
    public static  void putResponse(HttpServletResponse response){
        responseThreadLocal.set(response);
    }

    /**
     * 设置HttpServletRequest，HttpServletResponse
     * @param request
     * @param response
     */
    public  static void  put(HttpServletRequest request,HttpServletResponse response){
        putRequest(request);
        putResponse(response);
    }
    public static  void putUserId(String userId) {
    	userIdThreadLocal.set(userId);
    }
    
    public static  String getUserId() {
    	return userIdThreadLocal.get();
    }

    public static void putAdminUserId(String userId){
        adminUserIdThreadLocal.set(userId);
    }
    public static String getAdminUserId(){
        return adminUserIdThreadLocal.get();
    }
}
