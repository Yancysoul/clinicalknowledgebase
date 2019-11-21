package com.ywhk.ckb.service.impl;

import com.ywhk.ckb.dao.model.core.DiseaseListEntity;
import com.ywhk.ckb.dao.model.core.DiseaseTypeEntity;
import com.ywhk.ckb.dao.repository.DiseaseListRepository;
import com.ywhk.ckb.dao.repository.DiseaseTypeRepository;
import com.ywhk.ckb.service.DiseaseService;
import com.ywhk.ckb.service.dto.DiseaseListDto;
import com.ywhk.ckb.service.dto.DiseaseTypeDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("diseaseService")
public class DiseaseServicelmpl implements DiseaseService {

    @Autowired
    private DiseaseTypeRepository diseaseTypeRepository;
    @Autowired
    private DiseaseListRepository diseaseListRepository;

    /**
     * 查询疾病分类
     * @return
     */
    @Override
    public List<DiseaseTypeDto> queryDiseaseType() {
        return queryDiseaseByFParentID(0);
    }

    private List<DiseaseTypeDto> queryDiseaseByFParentID(Integer id){
        List<DiseaseTypeDto> ret = new ArrayList<>();
        List<DiseaseTypeEntity> diseases = diseaseTypeRepository.findByFParentID(id);
        for (DiseaseTypeEntity disease : diseases) {
            DiseaseTypeDto d = new DiseaseTypeDto();
            BeanUtils.copyProperties(disease,d);
            d.setChildrens(queryDiseaseByFParentID(disease.getFDiseaseTypeID()));
            ret.add(d);
        }
        return ret;
    }

    /**
     * 查询所有疾病
     * @return
     */
    @Override
    public List<DiseaseListDto> queryAllDisease () {
        List<DiseaseListDto> ret = new ArrayList<>();
        List<DiseaseListEntity> lists = diseaseListRepository.findAll();
        lists.forEach(list -> {
            DiseaseListDto d = new DiseaseListDto();
            BeanUtils.copyProperties(list, d);
            ret.add(d);
        });
        return ret;
    }

    /**
     * 根据疾病分类id查询对应疾病列表
     * @param diseaseListDto
     * @return
     */
    @Override
    public List<DiseaseListDto> queryDiseaseByTypeId (DiseaseListDto diseaseListDto) {
        List<DiseaseListDto> ret = new ArrayList<>();
        System.out.println(diseaseListDto.getFDiseaseTypeID());
        List<DiseaseListEntity> diseaseListEntities = diseaseListRepository.findByFDiseaseTypeID(diseaseListDto.getFDiseaseTypeID());
        diseaseListEntities.forEach(disease -> {
            DiseaseListDto d = new DiseaseListDto();
            BeanUtils.copyProperties(disease, d);
            ret.add(d);
        });
        return ret;
    }

    /**
     * 根据疾病id查询疾病详情
     * @param diseaseListEntity
     * @return
     */
    @Override
    public List<DiseaseListEntity> queryDiseaseDetailById (DiseaseListEntity diseaseListEntity) {
        return diseaseListRepository.findByFDiseaseID(diseaseListEntity.getFDiseaseID());
    }


}
