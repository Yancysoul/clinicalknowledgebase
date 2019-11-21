package com.ywhk.ckb.service.impl;

import com.ywhk.ckb.dao.model.core.UserEntity;
import com.ywhk.ckb.dao.repository.UserRepository;
import com.ywhk.ckb.service.UserService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: hpy
 * @date: 2019-10-11 15:59
 * @description:
 */
@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserEntity> queryUserList(UserEntity userEntity) {
        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(new UserEntity());
        userEntities.add(new UserEntity());
        log.info(userEntity.getFAge());
        return userEntities;
    }
}
