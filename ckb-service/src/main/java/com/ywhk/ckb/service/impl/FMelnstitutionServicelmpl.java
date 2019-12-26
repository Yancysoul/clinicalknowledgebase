package com.ywhk.ckb.service.impl;

import com.ywhk.ckb.common.http.response.PaginationResponse;
import com.ywhk.ckb.dao.model.core.FMelnstitutionEntity;
import com.ywhk.ckb.dao.repository.FMelnstitutionRepository;
import com.ywhk.ckb.service.FMelnstitutionService;
import com.ywhk.ckb.service.dto.fmelnstitution.*;
import com.ywhk.ckb.service.dto.user.DelUserRequest;
import com.ywhk.ckb.service.dto.user.DelUserResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service("fmelnstitutionService")
public class FMelnstitutionServicelmpl implements FMelnstitutionService {

    @Autowired
    private FMelnstitutionRepository  fMelnstitutionRepository;
    @Override
    /**
     * 机构列表
     * @return
     */
    public PaginationResponse<QueryFMelnstitutionListResponse> queryFMelnstitutionList(QueryFMelnstitutionListRequest request) {
        List<QueryFMelnstitutionListResponse> ret = new ArrayList<>();
        Page<FMelnstitutionEntity> fMelnstitutionEntities = fMelnstitutionRepository.findAll(request.getPageRequest());
        fMelnstitutionEntities.getContent().forEach(fMelnstitutionEntity -> {
            QueryFMelnstitutionListResponse d = new QueryFMelnstitutionListResponse();
            BeanUtils.copyProperties(fMelnstitutionEntity, d);
            ret.add(d);
        });
        return new PaginationResponse<>(fMelnstitutionEntities, ret);
    }

    /**
     * 查询所有机构列表
     * @return
     */
    public List<FMelnstitutionEntity> queryAllInstitu () {
        return fMelnstitutionRepository.findAll();
    }

    /**
     * 新增和修改机构
     * @return
     */
    @Override
    public UpdateFMelnstitutionResponse saveAndupdateFMelnstitutionList(UpdateFMelnstitutionResponse request) {
        FMelnstitutionEntity userEntity = null;
        if (request.getFMeInstitutionID() == null) {
            userEntity = new FMelnstitutionEntity();
            BeanUtils.copyProperties(request, userEntity);
        } else {
            userEntity = fMelnstitutionRepository.findByFMeInstitutionID(request.getFMeInstitutionID());
            if (userEntity == null) {
                throw new RuntimeException("当前人员不存在");
            } else {
                BeanUtils.copyProperties(request, userEntity);
            }
        }
        fMelnstitutionRepository.save(userEntity);
        return new UpdateFMelnstitutionResponse();
    }

    /**
     * 删除机构
     * @return
     */
    @Override
    public DelFMelnstitutionResponse delFMelnstitution(DelFMelnstitutionRequest request) {
        if (request.getFMeInstitutionID()!=null) {
            fMelnstitutionRepository.deleteById(request.getFMeInstitutionID());
        }else{
            throw new NullPointerException();
        }
        return new DelFMelnstitutionResponse();

    }


    /**
     * 批量删除机构
     * @return
     */
    @Override
    public void deleteInBatch(List<FMelnstitutionEntity> list) {
     fMelnstitutionRepository.deleteInBatch(list);
}


}
