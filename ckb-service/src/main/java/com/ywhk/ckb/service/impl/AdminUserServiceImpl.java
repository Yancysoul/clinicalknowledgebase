package com.ywhk.ckb.service.impl;

import com.ywhk.ckb.common.exception.BizException;
import com.ywhk.ckb.common.util.JWTUtils;
import com.ywhk.ckb.common.util.Md5Utils;
import com.ywhk.ckb.dao.model.core.AdminUserEntity;
import com.ywhk.ckb.dao.repository.AdminUserRepository;
import com.ywhk.ckb.service.AdminUserService;
import com.ywhk.ckb.service.dto.user.LoginRequest;
import com.ywhk.ckb.service.dto.user.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("adminUserService")
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {
    /**
     * 用户token过期时间(两小时)
     */
    private static final Long ADMIN_USER_TOKEN_EXPIRE_TIME = 2 * 60 * 60 * 1000L;
    @Autowired
    private AdminUserRepository adminUserRepository;

    @Override
    public LoginResponse login(LoginRequest request) {
        AdminUserEntity adminUserEntity = adminUserRepository.findFirstByFName(request.getFName());
        if (adminUserEntity == null) {
            throw new BizException("用户名或密码错误");
        }
        String password = adminUserEntity.getFPassword();
       // String requestPassword = Md5Utils.getMD5(request.getFName() + request.getFPassword());

        String requestPassword = request.getFPassword();
        if (!password.equals(requestPassword)) {
            throw new BizException("用户名或密码错误");
        }

        //创建后台用户token
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put(JWTUtils.USER_ID_KEY, adminUserEntity.getFUserid());
        String token = JWTUtils.createJWT(claims, "clinicalknowledgebase", ADMIN_USER_TOKEN_EXPIRE_TIME);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setFUserid(adminUserEntity.getFUserid());
        response.setFgroupid(adminUserEntity.getFgroupid());
        response.setMechanismID(adminUserEntity.getMechanismID());
        response.setFNumber(adminUserEntity.getFNumber());
        response.setFName(adminUserEntity.getFName());
        response.setFPassword(adminUserEntity.getFPassword());
        response.setFJob(adminUserEntity.getFJob());
        response.setFType(adminUserEntity.getFType());
        response.setFCreateDate(adminUserEntity.getFCreateDate());
        response.setFModifyDate(adminUserEntity.getFModifyDate());

        return response;
    }

}
