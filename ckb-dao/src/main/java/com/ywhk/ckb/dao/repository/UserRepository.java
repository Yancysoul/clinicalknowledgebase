package com.ywhk.ckb.dao.repository;

import com.ywhk.ckb.dao.model.core.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface UserRepository extends JpaRepository<UserEntity, Integer>, JpaSpecificationExecutor<UserEntity> {
    UserEntity findByFUserid(Integer id);
    UserEntity deleteByFUserid(Integer id);
    UserEntity findByFNameAndFPassword(String username,String password);

}
