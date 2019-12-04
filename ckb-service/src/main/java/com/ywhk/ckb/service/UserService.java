package com.ywhk.ckb.service;

import com.ywhk.ckb.common.http.response.PaginationResponse;
import com.ywhk.ckb.dao.model.core.UserEntity;
import com.ywhk.ckb.service.dto.user.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public interface UserService {
    PaginationResponse<QueryUserListResponse> queryUserList(QueryUserListRequest request);
    SaveUserResponse saveUser(SaveUserRequest request);
    DelUserResponse delUser (DelUserRequest request);

}
