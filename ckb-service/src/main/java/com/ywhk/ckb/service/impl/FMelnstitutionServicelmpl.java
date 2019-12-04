package com.ywhk.ckb.service.impl;

import com.ywhk.ckb.dao.model.core.FMelnstitutionEntity;
import com.ywhk.ckb.dao.repository.FMelnstitutionRepository;
import com.ywhk.ckb.service.FMelnstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("fmelnstitutionService")
public class FMelnstitutionServicelmpl implements FMelnstitutionService {

    @Autowired
    private FMelnstitutionRepository  fMelnstitutionRepository;
    @Override
    /**
     * 公司列表
     * @return
     */
    public List<FMelnstitutionEntity> queryFMelnstitutionList(String name) {
        return fMelnstitutionRepository.findAll();
    }
    /**
     * 删除公司
     * @return
     */
    @Override
    public void deleteById(Integer id) {
          fMelnstitutionRepository.deleteById(id);
    }
    /**
     * 批量删除公司
     * @return
     */
    @Override
    public void deleteInBatch(List<FMelnstitutionEntity> list) {
     fMelnstitutionRepository.deleteInBatch(list);
}

}
