package com.ywhk.ckb.common.http.response;

import com.ywhk.ckb.enums.http.CommonResultCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 接口层结果返回
 */
@Getter
@Setter
public class CommonResponse implements Serializable{

    private Integer code = CommonResultCode.SUCCESS.getCode();
    private String message = CommonResultCode.SUCCESS.getMessage();
    private Object data;

    public void serError(CommonResultCode commonResultCode){
        this.code = commonResultCode.getCode();
        this.message = commonResultCode.getMessage();
    }
}
