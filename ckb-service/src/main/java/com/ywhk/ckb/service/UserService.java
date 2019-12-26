package com.ywhk.ckb.service;

import com.ywhk.ckb.common.doc.annotation.ApiDocInterface;
import com.ywhk.ckb.common.doc.annotation.ApiDocService;
import com.ywhk.ckb.common.http.response.PaginationResponse;
import com.ywhk.ckb.service.dto.user.*;
import org.springframework.stereotype.Service;


@Service
@ApiDocService("人员服务")
public interface UserService {
    @ApiDocInterface("查询人员列表")
    PaginationResponse<QueryUserListResponse> queryUserList(QueryUserListRequest request);
    @ApiDocInterface("更新修改人员信息")
    SaveUserResponse saveUser(SaveUserRequest request);
    @ApiDocInterface("删除人员信息")
    DelUserResponse delUser (DelUserRequest request);
}
