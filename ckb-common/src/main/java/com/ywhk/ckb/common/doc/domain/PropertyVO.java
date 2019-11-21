package com.ywhk.ckb.common.doc.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class PropertyVO {
    private String name = "";
    private String type = "";
    private String desc = "";
    private Boolean require;
    private String remark = "";
    private FiledType filedType;
    private List<String> enumInfoList = Collections.emptyList();
    private List<PropertyVO> propertyVOList = Collections.EMPTY_LIST;

    public static enum FiledType{
        BASIC_TYPE,LIST,SELF_DEFINED,SIMPLE_LIST
    }
}
