package com.ywhk.ckb.service;

import com.ywhk.ckb.common.doc.annotation.ApiDocInterface;
import com.ywhk.ckb.common.doc.annotation.ApiDocService;
import com.ywhk.ckb.common.jwt.annotation.NoLogin;
import com.ywhk.ckb.service.dto.user.LoginRequest;
import com.ywhk.ckb.service.dto.user.LoginResponse;

@ApiDocService("后台管理用户")
public interface AdminUserService {
    @ApiDocInterface("用户登录")
    @NoLogin
    LoginResponse login (LoginRequest request);
}
