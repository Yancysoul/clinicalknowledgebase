package com.ywhk.ckb.common.http.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhuronghui
 * @date 2019年08月29日
 * @description 签字对象
 */
@Getter
@Setter
public class SignRequest {
    /**
     * 签字
     */
    private String sign;
    /**
     * 被签字的字符串，base64格式
     */
    private String data;
}
