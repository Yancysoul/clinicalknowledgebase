package com.ywhk.ckb.common.doc.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class ApiTestVO {
    private String error = "";
    private Integer maxListIndex = 0;
    private String currentService = "";
    private String currentMethod = "";
    private String requestUrl = "";
    private String content = "";
    private List<PropertyVO> propertyVOList = Collections.emptyList();
}
