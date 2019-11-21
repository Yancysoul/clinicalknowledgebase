package com.ywhk.ckb.common.http.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class PaginationResponse<T> implements Serializable{
    /**
     * 数据列表
     */
    private List<T> list = Collections.EMPTY_LIST;

    /**
     * 分页信息
     */
    private Pagination pagination;

    public PaginationResponse(){
        pagination = new Pagination();
    }

    public PaginationResponse(Page<T> page){
        this.pagination = new Pagination();
        this.pagination.setCurrentPage(page.getPageable().getPageNumber()+1);
        this.pagination.setPageSize(page.getPageable().getPageSize());
        this.pagination.setPages(page.getTotalPages());
        this.pagination.setTotal(page.getTotalElements());
        this.list = page.getContent();
    }
    public PaginationResponse(Page page,List<T> resultList){
        this.pagination = new Pagination();
        this.pagination.setCurrentPage(page.getPageable().getPageNumber()+1);
        this.pagination.setPageSize(page.getPageable().getPageSize());
        this.pagination.setPages(page.getTotalPages());
        this.pagination.setTotal(page.getTotalElements());
        this.list = resultList;
    }

    @Getter
    @Setter
    public static class Pagination<T>{

        public Pagination() {}

        private int currentPage = 1;
        private int pageSize = 10;
        private long total = 0;
        private int pages = 0;

        public Pagination(int currentPage, int pageSize) {
            this.currentPage = currentPage;
            this.pageSize = pageSize;
        }
    }

}
