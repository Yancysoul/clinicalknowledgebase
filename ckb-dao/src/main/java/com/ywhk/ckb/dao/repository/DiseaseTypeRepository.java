package com.ywhk.ckb.dao.repository;

import com.ywhk.ckb.dao.model.core.DiseaseTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DiseaseTypeRepository extends JpaRepository<DiseaseTypeEntity, Integer> {
    List<DiseaseTypeEntity> findByFParentID(Integer id);
}
