package com.ywhk.ckb.common.http.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseRequest{
    private String token;
    private Long timestamp = System.currentTimeMillis();
}
