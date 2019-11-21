//package com.ywhk.ckb.common.doc.controller;
//
//import com.ywhk.ckb.common.doc.core.DocBuilder;
//import com.ywhk.ckb.common.doc.core.ParamConverter;
//import com.ywhk.ckb.common.doc.domain.ApiTestVO;
//import com.ywhk.ckb.common.doc.domain.DocVO;
//import com.ywhk.ckb.common.doc.util.VelocityUtils;
//import lombok.SneakyThrows;
//import org.apache.velocity.VelocityContext;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.stereotype.Controller;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.net.URLEncoder;
//
//@Controller
//@RequestMapping("/apiDoc")
//public class ApiDocController implements ApplicationContextAware{
//
//    @Value("${doc.basePackage}")
//    private String basePackage;
//    private ApplicationContext applicationContext;
//
//    @RequestMapping("")
//    @SneakyThrows
//    public void doc(HttpServletRequest request,
//                    HttpServletResponse response,String currentService,
//                    String currentMethod){
//        response.setCharacterEncoding("UTF-8");
//        DocVO docVO = new DocBuilder().buildDoc(basePackage,currentService,currentMethod);
//        docVO.setRequestUrl(buildRequestUrl(request,currentService,currentMethod));
//        String content = VelocityUtils.getContent("/template/doc.vm",docVO);
//        content = getFullPage(content);
//        response.getWriter().write(content);
//    }
//
//    private String getFullPage(String content){
//        VelocityContext velocityContext = new VelocityContext();
//        velocityContext.put("content",content);
//        content = VelocityUtils.getContent("/template/basePage.vm",velocityContext);
//        return content;
//    }
//
//    private String buildRequestUrl(HttpServletRequest request,String currentService,
//                                   String currentMethod){
//        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" +
//                request.getServerPort() + request.getContextPath()+"/";
//        if(!StringUtils.isEmpty(currentService) && currentService.indexOf(".") != -1){
//            currentService = currentService.substring(currentService.lastIndexOf(".")+1);
//            currentService = currentService.substring(0,1).toLowerCase()+currentService.substring(1);
//        }
//        return baseUrl+"api/"+currentService+"/"+currentMethod;
//    }
//
//    @RequestMapping("/apiTestPage")
//    public void apiTestPage(String currentService,String currentMethod,HttpServletRequest request,HttpServletResponse response) throws Exception{
//        response.setCharacterEncoding("UTF-8");
//        if(StringUtils.isEmpty(currentService)
//                || StringUtils.isEmpty(currentMethod)){
//            response.getWriter().print("<html><meta charset=\"utf-8\"><body>请选择接口服务和接口方法后再进行测试<a href=\"javascript:history.back(-1);\">返回</a></body></html>");
//        }
//        ApiTestVO apiTestVO = new DocBuilder().buildApiTest(request,currentService,currentMethod);
//        apiTestVO.setRequestUrl(buildRequestUrl(request,currentService,currentMethod));
//        apiTestVO.setCurrentService(currentService);
//        apiTestVO.setCurrentMethod(currentMethod);
//        String content = VelocityUtils.getContent("/template/apiTest.vm",apiTestVO);
//        content = getFullPage(content);
//        response.getWriter().write(content);
//    }
//
//    @RequestMapping("/apiTest")
//    @ResponseBody
//    @SneakyThrows
//    public Object apiTest(HttpServletRequest request,HttpServletResponse response,String currentService,String currentMethod){
//
//        ParamConverter paramConverter = new ParamConverter();
//        String jsonParam =  paramConverter.getData(request);
//
//        //添加cookie
//        String cookieKey = paramConverter.getCookieKey(currentService,currentMethod);
//        Cookie cookie = new Cookie(cookieKey, URLEncoder.encode(jsonParam, "UTF-8"));
//        cookie.setMaxAge(30*24*60*60);
//        cookie.setPath("/");
//        response.addCookie(cookie);
//
//        return jsonParam;
//    }
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
//    }
//}
