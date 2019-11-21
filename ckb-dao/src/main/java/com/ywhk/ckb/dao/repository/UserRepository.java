package com.ywhk.ckb.dao.repository;

import com.ywhk.ckb.dao.model.core.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: hpy
 * @date: 2019-10-11 15:54
 * @description:
 */
public interface UserRepository extends JpaRepository<UserEntity,String> {

}
