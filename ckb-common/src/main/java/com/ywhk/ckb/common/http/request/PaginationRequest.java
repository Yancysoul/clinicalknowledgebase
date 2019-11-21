package com.ywhk.ckb.common.http.request;

import com.ywhk.ckb.common.doc.annotation.ApiDocElement;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

@Getter
@Setter
public abstract class PaginationRequest extends BaseRequest{
    @ApiDocElement("当前页")
    private int currentPage = 1;
    @ApiDocElement("每页记录数")
    private int pageSize = 10;
    public PageRequest getPageRequest(){
        //PageRequest第一页从0开始
        return PageRequest.of(this.getCurrentPage()-1,this.getPageSize());
    }
}
