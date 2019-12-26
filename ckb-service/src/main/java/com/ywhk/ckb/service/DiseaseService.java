package com.ywhk.ckb.service;

import com.ywhk.ckb.common.doc.annotation.ApiDocInterface;
import com.ywhk.ckb.common.doc.annotation.ApiDocService;
import com.ywhk.ckb.common.http.response.PaginationResponse;
import com.ywhk.ckb.dao.model.core.DiseaseListEntity;
import com.ywhk.ckb.dao.model.core.DiseaseTypeEntity;
import com.ywhk.ckb.service.dto.disease.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ApiDocService("疾病服务")
public interface DiseaseService {
    @ApiDocInterface("疾病类型查询")
    List<DiseaseTypeDto> queryDiseaseType();
    @ApiDocInterface("查询疾病列表")
    PaginationResponse<QueryAllDiseaseResponse> queryAllDisease(QueryAllDiseaseRequest request);
    @ApiDocInterface("根据疾病类型查询疾病")
    PaginationResponse<QueryDiseaseByTypeIdResponse> queryDiseaseByTypeId (QueryDiseaseByTypeIdRequest request);
    @ApiDocInterface("根据疾病名称查询疾病")
    PaginationResponse<QueryDiseaseByNameResponse> queryDiseaseByName (QueryDiseaseByNameRequest request);
    @ApiDocInterface("根据疾病类型和名称查询")
    PaginationResponse<QueryDiseaseByTypeIdAndNameResponse> queryDiseaseByTypeIdAndName (QueryDiseaseByTypeIdAndNameRequest request);
    @ApiDocInterface("根据疾病ID查询疾病详情")
    DiseaseListEntity queryDiseaseDetailById(DiseaseListEntity diseaseListEntity);
    @ApiDocInterface("更新修改疾病内容")
    DiseaseListEntity updateDiseaseListEntityID(DiseaseListEntity diseaseListEntity);
    @ApiDocInterface("删除疾病")
    void deletDiseaseListEntityById(Integer id);
    DelDiseaseResponse delDisease(DelDiseaseRequest request);

    PaginationResponse<QueryAllDiseaseResponse> queryDiseaseList(QueryAllDiseaseRequest request);
}
