package com.ywhk.ckb.service.impl;

import com.ywhk.ckb.common.http.response.PaginationResponse;
import com.ywhk.ckb.dao.model.core.DiseaseListEntity;
import com.ywhk.ckb.dao.model.core.DiseaseTypeEntity;
import com.ywhk.ckb.dao.repository.DiseaseListRepository;
import com.ywhk.ckb.dao.repository.DiseaseTypeRepository;
import com.ywhk.ckb.service.DiseaseService;
import com.ywhk.ckb.service.dto.disease.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
    public PaginationResponse<QueryAllDiseaseResponse> queryAllDisease (QueryAllDiseaseRequest request) {
        List<QueryAllDiseaseResponse> ret = new ArrayList<>();
        Page<DiseaseListEntity> lists = diseaseListRepository.findAll(request.getPageRequest());
        lists.getContent().forEach(list -> {
            QueryAllDiseaseResponse d = new QueryAllDiseaseResponse();
            BeanUtils.copyProperties(list, d);
            ret.add(d);
        });
        return new PaginationResponse<>(lists, ret);
    }

    /**
     * 根据疾病分类id查询对应疾病列表
     * @param request
     * @return
     */
    @Override
    public PaginationResponse<QueryDiseaseByTypeIdResponse> queryDiseaseByTypeId (QueryDiseaseByTypeIdRequest request) {
//        Sort sort = new Sort(Sort.Direction.DESC,"FCreateDate");
//        PageRequest pageRequest = PageRequest.of(request.getPageRequest().getPageNumber(), request.getPageRequest().getPageSize(), sort);
        List<QueryDiseaseByTypeIdResponse> ret = new ArrayList<>();
//        Page<DiseaseListEntity> diseaseListEntities = diseaseListRepository.findAll(diseaseSpecification(request), pageRequest);
        Page<DiseaseListEntity> diseaseListEntities = diseaseListRepository.findByFDiseaseTypeID(request.getFDiseaseTypeID(), request.getPageRequest());
        diseaseListEntities.getContent().forEach(disease -> {
            QueryDiseaseByTypeIdResponse d = new QueryDiseaseByTypeIdResponse();
            BeanUtils.copyProperties(disease, d);
            ret.add(d);
        });
        return new PaginationResponse<>(diseaseListEntities,ret);
    }

    /**
     *
     * 疾病列表查询（疾病名称模糊查询）
     * @param request
     * @return
     */
    @Override
    public PaginationResponse<QueryDiseaseByNameResponse> queryDiseaseByName (QueryDiseaseByNameRequest request) {
        List<QueryDiseaseByNameResponse> ret = new ArrayList<>();
        Page<DiseaseListEntity> entities = diseaseListRepository.findByFDiseaseNameLike( "%" + request.getFDiseaseName() + "%", request.getPageRequest());
        entities.getContent().forEach(entitie -> {
            QueryDiseaseByNameResponse d = new QueryDiseaseByNameResponse();
            BeanUtils.copyProperties(entitie, d);
            ret.add(d);
        });
        return new PaginationResponse<>(entities, ret);
    }

    /**
     * 根据类别和名称查询
     * @param request
     * @return
     */
    @Override
    public PaginationResponse<QueryDiseaseByTypeIdAndNameResponse> queryDiseaseByTypeIdAndName (QueryDiseaseByTypeIdAndNameRequest request) {
        List<QueryDiseaseByTypeIdAndNameResponse> ret = new ArrayList<>();
        Page<DiseaseListEntity> entities = diseaseListRepository.findByFDiseaseTypeIDAndFDiseaseNameLike(request.getFDiseaseTypeID(), "%" + request.getFDiseaseName() + "%", request.getPageRequest());
        entities.getContent().forEach(entitie -> {
            QueryDiseaseByTypeIdAndNameResponse d = new QueryDiseaseByTypeIdAndNameResponse();
            BeanUtils.copyProperties(entitie, d);
            ret.add(d);
        });
        return new PaginationResponse<>(entities, ret);
    }

    /**
     * 根据疾病id查询疾病详情
     * @param diseaseListEntity
     * @return
     */
    @Override
    public DiseaseListEntity queryDiseaseDetailById (DiseaseListEntity diseaseListEntity) {
        return diseaseListRepository.findByFDiseaseID(diseaseListEntity.getFDiseaseID());
    }


    @SuppressWarnings("serial")
    private Specification<DiseaseListEntity> diseaseSpecification(QueryDiseaseByTypeIdRequest request) {
        return new Specification<DiseaseListEntity>() {
            public Predicate toPredicate(Root<DiseaseListEntity> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                Predicate predicate = builder.conjunction();

                if(!StringUtils.isEmpty(request.getFDiseaseTypeID())){
                    predicate.getExpressions().add(builder.equal(root.get("FDiseaseTypeID"), request.getFDiseaseTypeID()));
                }
                return predicate;
            }
        };
    }


    /**
     * 疾病新增，修改
     * @return
     */
    @Override
    public DiseaseListEntity updateDiseaseListEntityID(DiseaseListEntity diseaseListEntity) {
        Integer chinaMaladEntityId = diseaseListEntity.getFDiseaseID();
        diseaseListRepository.save(diseaseListEntity);
        return (DiseaseListEntity) diseaseListRepository.findByFDiseaseID(chinaMaladEntityId);
    }



    /**
     * 疾病列表删除
     * @return
     */
    @Override
    public void deletDiseaseListEntityById(Integer id) {
        diseaseListRepository.deleteById(id);
    }

    @Override
    public DelDiseaseResponse delDisease(DelDiseaseRequest request) {
        if (StringUtils.isEmpty(request.getFDiseaseID())) {
            System.out.println("当前疾病不存在");
            return null;
        }
        System.out.println(request.getFDiseaseID());
        diseaseListRepository.deleteById(request.getFDiseaseID());
        return new DelDiseaseResponse();
    }

    /**
     * 查询所有药品 以及模糊查询
     * @param request
     * @return
     */
    @Override
    public PaginationResponse<QueryAllDiseaseResponse> queryDiseaseList(QueryAllDiseaseRequest request) {
        List<QueryAllDiseaseResponse> ret = new ArrayList<>();
        Page<DiseaseListEntity> lists;
            if (request.getFDiseaseTypeID() != null && request.getFDiseaseName() != null) {
                lists = diseaseListRepository.findByFDiseaseTypeIDAndFDiseaseNameLike(request.getFDiseaseTypeID(), "%" + request.getFDiseaseName() + "%", request.getPageRequest());
            } else if (request.getFDiseaseTypeID() != null) {
                lists = diseaseListRepository.findByFDiseaseTypeID(request.getFDiseaseTypeID(), request.getPageRequest());
            } else if (request.getFDiseaseName() != null) {
                lists = diseaseListRepository.findByFDiseaseNameLike("%" + request.getFDiseaseName() + "%", request.getPageRequest());
            } else {
                lists = diseaseListRepository.findAll(request.getPageRequest());
            }
            lists.getContent().forEach(list -> {
                QueryAllDiseaseResponse d = new QueryAllDiseaseResponse();
                BeanUtils.copyProperties(list, d);
                ret.add(d);
            });
        return new PaginationResponse<>(lists, ret);
    }


}
