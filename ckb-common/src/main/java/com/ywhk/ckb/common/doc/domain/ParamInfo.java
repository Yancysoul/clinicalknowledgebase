package com.ywhk.ckb.common.doc.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhurh on 17/7/18.
 */
@Getter
@Setter
public class ParamInfo {
    private Map<String,Integer> listInfo = new HashMap<>();
    private Map<String,String> params = new HashMap<>();
    private Integer maxListIndex = 1;

}
