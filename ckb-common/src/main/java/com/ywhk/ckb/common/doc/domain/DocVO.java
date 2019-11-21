package com.ywhk.ckb.common.doc.domain;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class DocVO{
    private String error = "";
    private List<Element> serviceList = Collections.emptyList();
    private List<Element> methodList = Collections.emptyList();
    private String currentService = "";
    private String currentMethod = "";
    private Class<?> selectedService;
    private Method selectedMethod;
    private String requestUrl = "";
    //返回样例
    private String responseExample = "";
    private String requestContent = "";
    private String responseContent= "";

    @Getter
    @Setter
    public static class Element{
        private String name = "";
        private String desc = "";
    }
}
