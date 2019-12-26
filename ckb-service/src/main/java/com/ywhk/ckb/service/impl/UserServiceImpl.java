package com.ywhk.ckb.service.impl;

import com.ywhk.ckb.common.http.response.CommonResponse;
import com.ywhk.ckb.common.http.response.PaginationResponse;
import com.ywhk.ckb.dao.model.core.UserEntity;
import com.ywhk.ckb.dao.repository.FMelnstitutionRepository;
import com.ywhk.ckb.dao.repository.GroupRepostory;
import com.ywhk.ckb.dao.repository.UserRepository;
import com.ywhk.ckb.service.UserService;
import com.ywhk.ckb.service.dto.user.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: hpy
 * @date: 2019-10-11 15:59
 * @description:
 */
@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepostory groupRepostory;
    @Autowired
    private FMelnstitutionRepository fMelnstitutionRepository;

    /**
     * 查询人员列表
     * @return
     */
    @Override
    public PaginationResponse<QueryUserListResponse> queryUserList(QueryUserListRequest request) {
        List<QueryUserListResponse> ret = new ArrayList<>();
        Page<UserEntity> lists = userRepository.findAll(request.getPageRequest());
        lists.getContent().forEach(list -> {
            QueryUserListResponse d = new QueryUserListResponse();
            BeanUtils.copyProperties(list, d);
            if (list.getFgroupid() != null) {
                d.setFgroupName(groupRepostory.findByFGROUPID(list.getFgroupid()).getFName());
            }
            if (list.getMechanismID() != null) {
                d.setMechanismName(fMelnstitutionRepository.findByFMeInstitutionID(list.getMechanismID()).getFName());
            }
            ret.add(d);
        });
        return new PaginationResponse<>(lists, ret);
    }

    /**
     * 修改、保存人员信息
     *
     * @param request
     * @return
     */
    @Override
    public SaveUserResponse saveUser(SaveUserRequest request) {
        Date date = new Date();
        //CommonResponse commonResponse = new CommonResponse();
        UserEntity userEntity = null;
        if (request.getFUserid() == null) {
            userEntity = new UserEntity();
            BeanUtils.copyProperties(request, userEntity);
            request.setFCreateDate(date);
        } else {
            userEntity = userRepository.findByFUserid(request.getFUserid());
            if (userEntity == null) {
                throw new RuntimeException("当前人员不存在");
            } else {
                BeanUtils.copyProperties(request, userEntity);
            }
        }
        request.setFModifyDate(date);
        userRepository.save(userEntity);
        return new SaveUserResponse();
    }

    /**
     * 删除人员
     * @param request
     * @return
     */
    @Override
    public DelUserResponse delUser (DelUserRequest request) {
        if (StringUtils.isEmpty(request.getFUserid())) {
            throw new NullPointerException("空指针异常");
        }
        userRepository.deleteById(request.getFUserid());
        return new DelUserResponse();
    }

}
