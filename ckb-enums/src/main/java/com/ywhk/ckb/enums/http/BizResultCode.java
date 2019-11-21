package com.ywhk.ckb.enums.http;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BizResultCode {

    SUCCESS(200,"成功"),
    FAIL(-1,"失败");

    private Integer code;
    private String message;
}
