package com.ywhk.ckb.common.http.response;

import com.ywhk.ckb.enums.http.BizResultCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BaseResponse implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5064784154744582596L;
	/**
     * 响应码 200-success -1-fail
     */
    private Integer code = BizResultCode.SUCCESS.getCode();
    /**
     * 错误信息
     */
    private String message = BizResultCode.SUCCESS.getMessage();
    private Object data = new Object();
}
