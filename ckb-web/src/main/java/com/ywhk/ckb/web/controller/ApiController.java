package com.ywhk.ckb.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.ywhk.ckb.common.http.response.BaseResponse;
import com.ywhk.ckb.common.http.response.CommonResponse;
import com.ywhk.ckb.common.threadlocal.ServletThreadLocal;
import com.ywhk.ckb.enums.http.CommonResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class ApiController implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private static Map<String,Object> serviceCache = new HashMap<>();

    @RequestMapping(value = "/{serviceName}/{method}",method= RequestMethod.POST)
    public Object post(@PathVariable("serviceName") String serviceName,
                       @PathVariable("method") String method,
                       @RequestBody String params,
                       HttpServletRequest request,
                       HttpServletResponse response){
        //将request,response放入threadLocal，方便后续代码调用使用
        ServletThreadLocal.put(request,response);
        log.info("调用接口[{}.{}]请求参数：{}",serviceName,method,params);
        return doService(serviceName,method,params,request);
    }

    private Object doService(String serviceName,String method,String params,HttpServletRequest request){

        CommonResponse commonResponse = new CommonResponse();
        Object serviceBean = getServiceBean(serviceName);
        if(serviceBean == null){
            commonResponse.serError(CommonResultCode.SERVICE_NOT_FOUND);
            return commonResponse;
        }

        //当前方法
        Method currentMethod = null;
        Method[] methods = serviceBean.getClass().getDeclaredMethods();
        for (Method method1 : methods){
            if(method1.getName().equals(method)){
                currentMethod = method1;
            }
        }
        if(currentMethod == null){
            commonResponse.serError(CommonResultCode.METHOD_NOT_FOUND);
            return commonResponse;
        }

        Object callResult = null;
        Type[] paramTypes=currentMethod.getGenericParameterTypes();
        try {
            //检查是否登录
//            if(!checkLogin(serviceBean,method,request)){
//                commonResponse.serError(CommonResultCode.NOT_LOGIN);
//                log.info("调用接口[{}.{}]响应结果：{}",serviceName,method, JSONObject.toJSONString(commonResponse));
//                return commonResponse;
//            }

            //检查是否验签
//            Sign signAnnotation = getAnnotation(serviceBean,method,Sign.class);
//            if(signAnnotation != null){
//                SignRequest signRequest = JSONObject.parseObject(params,SignRequest.class);
//                String data = signRequest.getData();
//                String sign = signRequest.getSign();
//                String curSign = Md5Utils.getMD5(data + "123");
//                if(!curSign.equals(sign)){
//                    commonResponse.serError(CommonResultCode.SIGN_ERROR);
//                    log.info("调用接口[{}.{}]响应结果：{}",serviceName,method,JSONObject.toJSONString(commonResponse));
//                    return commonResponse;
//                }else{
//                    params = Base64Utils.decodeBase64(data);
//                    log.info("调用接口[{}.{}]为需验签接口，实际请求参数为：{}",serviceName,method,params);
//                }
//            }

            if (paramTypes.length == 0) {
                callResult = currentMethod.invoke(serviceBean);
                commonResponse.setData(callResult);
            } else if (paramTypes.length == 1) {
                Object inputParam = JSONObject.parseObject(params,paramTypes[0]);
                //调用服务方法
                callResult = currentMethod.invoke(serviceBean, inputParam);
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setData(callResult);
                commonResponse.setData(baseResponse);
            } else {
                commonResponse.serError(CommonResultCode.PARAM_ERROR);
            }
        }catch (IllegalAccessException |IllegalArgumentException | InvocationTargetException e){
            if(e instanceof InvocationTargetException){
//                Throwable throwable = e.getCause();
////                if(throwable instanceof BizException){
////                    BaseResponse baseResponse = new BaseResponse();
////                    baseResponse.setCode(BizResultCode.FAIL.getCode());
////                    baseResponse.setMessage(throwable.getMessage());
////                    commonResponse.setData(baseResponse);
////                }else{
////                    commonResponse.serError(CommonResultCode.UNKNOWN_ERROR);
////                    log.error("服务[{}.{}]处理失败",serviceName,method,e);
////                }
            }else{
                commonResponse.serError(CommonResultCode.UNKNOWN_ERROR);
                log.error("服务[{}.{}]处理失败",serviceName,method,e);
            }
        }
        log.info("调用接口[{}.{}]响应结果：{}",serviceName,method,JSONObject.toJSONString(commonResponse));
        return commonResponse;

    }

    private Object getServiceBean(String serviceName){
        Object serviceBean = serviceCache.get(serviceName);
        try {
            if(serviceBean == null){
                //全路径匹配
                if(serviceName.contains(".")){
                    Class<?> apiClass = null;
                    try {
                        apiClass = Class.forName(serviceName);
                        serviceBean = this.applicationContext.getBean(apiClass);
                    }catch (ClassNotFoundException e){
                        log.error("获取服务["+serviceName+"]出错",e);
                    }catch (Exception e){
                        log.error("获取服务["+serviceName+"]出错",e);
                    }
                }else{
                    serviceBean = this.applicationContext.getBean(serviceName);
                }
            }
        }catch (Exception e){
            log.error("获取服务["+serviceName+"]出错",e);
        }
        if(serviceBean != null){
            serviceCache.put(serviceName,serviceBean);
        }
        return serviceBean;
    }

    /**
     * 检查是否登录
     * 方法带NoLogin注解表示不登录，AdminLogin表示后台接口需要登录，不带注解表示微信公众号接口需要登录
     * @param request
     * @return
     */
//    private boolean checkLogin(Object serviceBean, String method,HttpServletRequest request){
//        NoLogin noLogin = getAnnotation(serviceBean,method, NoLogin.class);
//        if(noLogin != null){
//            return true;
//        }
//        String token = request.getHeader("token");
//        if (StringUtils.isEmpty(token)) {
//            return false;
//        }
//        //验证登录信息是否过期
//        Claims claims;
//        try {
//            claims = JWTUtils.parseJWT(token);
//        }catch (Exception e){
//            return false;
//        }
//        long expiration = Long.valueOf(claims.get(Claims.EXPIRATION).toString());
//        if(System.currentTimeMillis()/1000 > expiration){
//            return false;
//        }
//        Object userType = claims.get(JWTUtils.USER_TYPE_KEY);
//        UserTypeEnum userTypeEnum = UserTypeEnum.valueOfCode(userType.toString());
//        //验证后端接口是否登录
//        AdminLogin adminLogin = getAnnotation(serviceBean,method,AdminLogin.class);
//        if(adminLogin != null){
//            if(userTypeEnum == UserTypeEnum.ADMIN_USER){
//                String userId = (String) claims.get(JWTUtils.USER_ID_KEY);
//                ServletThreadLocal.putAdminUserId(userId);
//                return true;
//            }else{
//                return false;
//            }
//        }
//
//        //验证前端接口是否登录
//        if(userTypeEnum == UserTypeEnum.WX_USER || userTypeEnum == UserTypeEnum.H5_USER){
//            String userId = (String) claims.get(JWTUtils.USER_ID_KEY);
//            ServletThreadLocal.putUserId(userId);
//            return true;
//        }else{
//            return false;
//        }
//    }

    /**
     * 获取接口注解
     * @param serviceBean
     * @param method
     * @param annotationClass
     * @param <T>
     * @return
     */
    private <T extends Annotation> T getAnnotation(Object serviceBean, String method, Class<T> annotationClass){

        Class<?> realBeanClass = AopUtils.getTargetClass(serviceBean);
        Class<?>[] interfaces = realBeanClass.getInterfaces();

        Class<?> apiInterface = null;
        if(interfaces != null && interfaces.length > 0){
            for (Class<?> currentInterface : interfaces){
                if(currentInterface.getName().contains("com.ywhk.ckb")){
                    apiInterface = currentInterface;
                    break;
                }
            }
        }
        if(apiInterface == null){
            return null;
        }
        Method[] methods = apiInterface.getDeclaredMethods();
        Method currentMethod = null;
        for (Method method1 : methods){
            if(method1.getName().equals(method)){
                currentMethod = method1;
            }
        }
        if(currentMethod == null){
            return null;
        }
        return currentMethod.getAnnotation(annotationClass);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}