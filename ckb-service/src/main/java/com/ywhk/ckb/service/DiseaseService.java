package com.ywhk.ckb.service;

import com.ywhk.ckb.dao.model.core.DiseaseListEntity;
import com.ywhk.ckb.service.dto.DiseaseListDto;
import com.ywhk.ckb.service.dto.DiseaseTypeDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DiseaseService {
    List<DiseaseTypeDto> queryDiseaseType();
    List<DiseaseListDto> queryAllDisease();
    List<DiseaseListDto> queryDiseaseByTypeId(DiseaseListDto diseaseListDto);
    List<DiseaseListEntity> queryDiseaseDetailById(DiseaseListEntity diseaseListEntity);
}
