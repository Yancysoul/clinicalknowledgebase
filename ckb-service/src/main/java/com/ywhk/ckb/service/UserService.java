package com.ywhk.ckb.service;

import com.ywhk.ckb.dao.model.core.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: hpy
 * @date: 2019-10-11 15:56
 * @description:
 */
@Service
public interface UserService {
    List<UserEntity> queryUserList(UserEntity userEntity);
}
