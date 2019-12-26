package com.ywhk.ckb.dao.repository;

import com.ywhk.ckb.dao.model.core.AdminUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AdminUserRepository extends JpaRepository<AdminUserEntity,String>,
        PagingAndSortingRepository<AdminUserEntity,String> {
    AdminUserEntity findFirstByFName(String FName);


}
