package com.ywhk.ckb.common.doc.core;

import com.google.gson.Gson;
import com.ywhk.ckb.common.doc.annotation.ApiDocElement;
import com.ywhk.ckb.common.doc.annotation.ApiDocIgnore;
import com.ywhk.ckb.common.doc.annotation.ApiDocInterface;
import com.ywhk.ckb.common.doc.annotation.ApiDocService;
import com.ywhk.ckb.common.doc.domain.ApiTestVO;
import com.ywhk.ckb.common.doc.domain.DocVO;
import com.ywhk.ckb.common.doc.domain.ParamInfo;
import com.ywhk.ckb.common.doc.domain.PropertyVO;
import com.ywhk.ckb.common.http.request.PaginationRequest;
import com.ywhk.ckb.common.http.response.BaseResponse;
import com.ywhk.ckb.common.http.response.PaginationResponse;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.*;
import java.util.*;

public class DocBuilder {

    private static String companyBasePackage = "com.zhilian.wxplatform";

    public DocVO buildDoc(String basePackage, String currentService, String currentMethod){

        DocVO docVO = new DocVO();

        if(StringUtils.isEmpty(basePackage)){
            docVO.setError("接口路径为空，请在properties文件或yml文件设置doc.basePackage参数");
            return docVO;
        }

        String[] packages = basePackage.split(",");
        Set<Class<?>> classSet = new LinkedHashSet<>();
        ClassScanner classScanner = new ClassScanner();
        for (String str : packages){
            Set<Class<?>> classes = classScanner.getClasses(str);
            for (Class clazz : classes){
                if(clazz.isInterface()){
                    classSet.add(clazz);
                }
            }
        }

        //获取全部的类文件
        List<DocVO.Element> serviceList = new ArrayList<>();
        Class<?> selectedService = null;
        for (Class<?> clazz : classSet){
            ApiDocIgnore apiDocIgnore = clazz.getAnnotation(ApiDocIgnore.class);
            if(apiDocIgnore != null){
                continue;
            }
            DocVO.Element element = new DocVO.Element();
            element.setName(clazz.getName());
            ApiDocService apiDocService = clazz.getAnnotation(ApiDocService.class);
            if(apiDocService != null){
                element.setDesc(apiDocService.value());
            }
            serviceList.add(element);
            if(clazz.getName().equals(currentService)){
                selectedService = clazz;
            }
        }

        //获取全部的方法
        Method selectedMethod = null;
        List<DocVO.Element> methodList = new ArrayList<>();
        if(selectedService != null){
            Method[] methods = selectedService.getDeclaredMethods();
            for (Method method : methods){
                ApiDocIgnore apiDocIgnore = method.getAnnotation(ApiDocIgnore.class);
                if(apiDocIgnore != null){
                    continue;
                }
                DocVO.Element element = new DocVO.Element();
                element.setName(method.getName());
                ApiDocInterface apiDocInterface = method.getAnnotation(ApiDocInterface.class);
                if(apiDocInterface != null){
                    element.setDesc(apiDocInterface.value());
                }
                methodList.add(element);
                if(method.getName().equals(currentMethod)){
                    selectedMethod = method;
                }
            }
        }

        if(selectedMethod != null){
            buildProperty(docVO,selectedMethod);
        }

        docVO.setServiceList(serviceList);
        docVO.setMethodList(methodList);
        docVO.setCurrentService(currentService==null?"":currentService);
        docVO.setCurrentMethod(currentMethod == null?"":currentMethod);
        docVO.setSelectedService(selectedService);
        docVO.setSelectedMethod(selectedMethod);
        return docVO;
    }

    private void buildProperty(DocVO docVO,Method method){

        TemplateExtractor templateExtractor = new TemplateExtractor();

        //获取请求参数，只获取第一个参数
        Class requestClass = method.getParameterTypes()[0];
        Map<String,Integer> hasBuildClassMap = new HashMap<>();
        List<PropertyVO> requestPropertyVOList = buildProperty(requestClass,hasBuildClassMap);
        docVO.setRequestContent(templateExtractor.buildPropertyContent(requestPropertyVOList));

        Class returnClass = method.getReturnType();

        if(returnClass == List.class){
            String responseExample = "业务层返回样例："+new Gson().toJson(new BaseResponse());
            responseExample += ",其中data为以下字段组成的json列表";
            docVO.setResponseExample(responseExample);
        }else if(returnClass == PaginationResponse.class){
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setData(new PaginationResponse<>());
            String responseExample = "业务层返回样例："+new Gson().toJson(baseResponse);
            responseExample += ",其中list为以下字段组成的json列表";
            docVO.setResponseExample(responseExample);
        }else{
            String responseExample = "业务层返回样例："+new Gson().toJson(new BaseResponse());
            responseExample += ",其中data为以下字段组成的json串";
            docVO.setResponseExample(responseExample);
        }
        List<PropertyVO> responsePropertyVOList = null;
        Type genericReturnType = method.getGenericReturnType();
        if (genericReturnType instanceof ParameterizedType) {
            ParameterizedType target = (ParameterizedType) genericReturnType;
            Type[] parameters = target.getActualTypeArguments();
            Class<?> responseClass = (Class<?>) parameters[0];
            hasBuildClassMap = new HashMap<>();
            responsePropertyVOList = buildProperty(responseClass,hasBuildClassMap);
        }else{
            hasBuildClassMap = new HashMap<>();
            responsePropertyVOList = buildProperty(returnClass,hasBuildClassMap);
        }
        docVO.setResponseContent(templateExtractor.buildPropertyContent(responsePropertyVOList));
    }

    private List<PropertyVO> buildProperty(Class clazz, Map<String,Integer> hasBuildClassMap){
        List<PropertyVO> propertyVOList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        List<Field> fieldList = new ArrayList<>(Arrays.asList(fields));
        //检查是否是分页类
        if(clazz.getSuperclass() == PaginationRequest.class){
            Field[] superFields = clazz.getSuperclass().getDeclaredFields();
            fieldList.addAll(new ArrayList<>(Arrays.asList(superFields)));
        }
        for (Field field : fieldList){
            if(field.getName().equals("serialVersionUID")){
                continue;
            }
            PropertyVO propertyVO = new PropertyVO();
            if(field.getType() == List.class){
                buildProperty(field,propertyVO);
                propertyVO.setFiledType(PropertyVO.FiledType.LIST);
                //获取list的泛型
                Type type = field.getGenericType();
                if (type instanceof ParameterizedType) {
                    ParameterizedType target = (ParameterizedType) type;
                    Type[] parameters = target.getActualTypeArguments();
                    Class<?> modelClass = (Class<?>) parameters[0];
                    //非自定义对象类型直接返回，不允许此种
                    if(modelClass.toString().contains(companyBasePackage)){
                        if(hasBuildClassMap.containsKey(modelClass.getName())){
                            hasBuildClassMap.put(modelClass.getName(),hasBuildClassMap.get(modelClass.getName())+1);
                        }else{
                            hasBuildClassMap.put(modelClass.getName(),1);
                        }
                        if(hasBuildClassMap.get(modelClass.getName()) > 2){
                            continue;
                        }
                        List<PropertyVO> propertyVOList1 = buildProperty(modelClass,hasBuildClassMap);
                        propertyVO.setPropertyVOList(propertyVOList1);
                        propertyVO.setRemark("以下包含"+field.getName()+"[0].的后面字段组成的json列表");
                    }else{
                        propertyVO.setFiledType(PropertyVO.FiledType.SIMPLE_LIST);
                        propertyVO.setRemark(modelClass.getSimpleName()+"类型列表");
                    }
                }
            }else if(field.getType().toString().contains(companyBasePackage)){
                propertyVO.setFiledType(PropertyVO.FiledType.SELF_DEFINED);
                buildProperty(field,propertyVO);
                Class<?> propertyClass = field.getType();
                List<PropertyVO> propertyVOList1 = buildProperty(propertyClass,hasBuildClassMap);
                propertyVO.setPropertyVOList(propertyVOList1);
            }else{
                buildProperty(field,propertyVO);
                propertyVO.setFiledType(PropertyVO.FiledType.BASIC_TYPE);
            }
            propertyVOList.add(propertyVO);
        }

        return propertyVOList;
    }

    private PropertyVO buildProperty(Field field,PropertyVO propertyVO){
        if(propertyVO == null){
            propertyVO = new PropertyVO();
        }
        ApiDocElement apiDocElement = field.getAnnotation(ApiDocElement.class);
        String name = field.getName();
        String type = field.getType().getSimpleName();
        String desc = "";
        String remark = "";
        //默认必输
        Boolean require = true;
        if(apiDocElement != null){
            desc = apiDocElement.value();
            require = apiDocElement.required();
            Class<?> enumClass = apiDocElement.enumClass();
            if(enumClass != null){
                List<String> enumInfoList = extractEnum(enumClass);
                StringBuilder stringBuilder = new StringBuilder();
                enumInfoList.forEach(item -> {
                    stringBuilder.append(item).append(",");
                });
                remark = stringBuilder.toString();
                if(remark.endsWith(",")){
                    remark = remark.substring(0,remark.length()-1);
                }
                propertyVO.setEnumInfoList(enumInfoList);
            }
        }
        propertyVO.setName(name);
        propertyVO.setType(type);
        propertyVO.setDesc(desc);
        propertyVO.setRequire(require);
        propertyVO.setRemark(remark);
        return propertyVO;
    }

    /**
     * 根据传入的枚举类型提取描述信息
     *
     * @param enumClass
     * @return
     */
    List<String> extractEnum(Class<?> enumClass) {
        Object[] values = enumClass.getEnumConstants();
        Method getCodeMethod = null;
        Method getDescMethod = null;
        try {
            getCodeMethod = enumClass.getMethod("getCode");
            getDescMethod = enumClass.getMethod("getDesc");
        } catch (NoSuchMethodException e) {
            return new ArrayList<>();
        }
        if (getCodeMethod != null && getDescMethod != null) {
            List<String> list = new ArrayList<>();
            for (Object val : values) {
                try {
                    Object code = getCodeMethod.invoke(val);
                    Object desc = getDescMethod.invoke(val);
                    list.add(code + ":" + desc);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }
        return new ArrayList<>();
    }
    @SneakyThrows
    public ApiTestVO buildApiTest(HttpServletRequest request,String currentService, String currentMethod){
        Class<?> clazz = Class.forName(currentService);
        Method[] methods = clazz.getDeclaredMethods();
        Method selectedMethod = null;
        for (Method method : methods){
            if(method.getName().equals(currentMethod)){
                selectedMethod = method;
                break;
            }
        }
        Class requestClass = selectedMethod.getParameterTypes()[0];
        Map<String,Integer> hasBuildClassMap = new HashMap<>();
        List<PropertyVO> requestPropertyVOList = buildProperty(requestClass,hasBuildClassMap);
        TemplateExtractor templateExtractor = new TemplateExtractor();
        StringBuilder content = new StringBuilder();

        ParamConverter paramConverter = new ParamConverter();
        ParamInfo paramInfo = paramConverter.parseJsonToParam(request,currentService,currentMethod);

        templateExtractor.buildTestContent(requestPropertyVOList,content,"",paramInfo);
        ApiTestVO apiTestVO = new ApiTestVO();
        apiTestVO.setContent(content.toString());
        apiTestVO.setMaxListIndex(paramInfo.getMaxListIndex());
        return apiTestVO;
    }
}
