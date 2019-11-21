package com.ywhk.ckb.enums.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonResultCode {

    SUCCESS(200,"成功"),
    NOT_LOGIN(401,"未登录或登录信息已过期"),
    NOT_AUTH(403,"未授权"),
    SERVICE_NOT_FOUND(-100,"服务不存在"),
    METHOD_NOT_FOUND(-101,"方法不存在"),
    PARAM_ERROR(-103,"参数格式错误"),
    UNKNOWN_ERROR(-104,"系统异常"),
    SIGN_ERROR(-105,"签名验证失败");

    private int code;
    private String message;

}
