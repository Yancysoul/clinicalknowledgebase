package com.ywhk.ckb.service;

import com.ywhk.ckb.common.http.response.PaginationResponse;
import com.ywhk.ckb.dao.model.core.DiseaseListEntity;
import com.ywhk.ckb.service.dto.disease.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DiseaseService {
    List<DiseaseTypeDto> queryDiseaseType();
    PaginationResponse<QueryAllDiseaseResponse> queryAllDisease(QueryAllDiseaseRequest request);
    PaginationResponse<QueryDiseaseByTypeIdResponse> queryDiseaseByTypeId (QueryDiseaseByTypeIdRequest request);
    PaginationResponse<QueryDiseaseByNameResponse> queryDiseaseByName (QueryDiseaseByNameRequest request);
    PaginationResponse<QueryDiseaseByTypeIdAndNameResponse> queryDiseaseByTypeIdAndName (QueryDiseaseByTypeIdAndNameRequest request);
    List<DiseaseListEntity> queryDiseaseDetailById(DiseaseListEntity diseaseListEntity);
    DiseaseListEntity updateDiseaseListEntityID(DiseaseListEntity diseaseListEntity);
    void deletDiseaseListEntityById(Integer id);
    DelDiseaseResponse delDisease(DelDiseaseRequest request);

    PaginationResponse<QueryAllDiseaseResponse> queryDiseaseList(QueryAllDiseaseRequest request);
}
