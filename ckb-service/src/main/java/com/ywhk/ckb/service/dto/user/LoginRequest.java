package com.ywhk.ckb.service.dto.user;

import com.ywhk.ckb.common.doc.annotation.ApiDocElement;
import com.ywhk.ckb.common.http.request.BaseRequest;
import lombok.Data;

@Data
public class LoginRequest extends BaseRequest {
    @ApiDocElement("用户名")
    private String FName;

    @ApiDocElement("密码")
    private String FPassword;

}
