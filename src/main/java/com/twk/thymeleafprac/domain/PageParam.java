package com.twk.thymeleafprac.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PageParam {
    int curPage; // 현재페이지(1~5)
    int limit; // 한페이지에 보여줄 게시물 수
    int offset; // 시작 row
    int paginationCnt; // 한 페이지에 표시할 페이지네이션 개수
    int curpaginationCnt; // 현재 페이지네이션 개수
    int totalItemCnt; // 컨텐츠 총 개수
    int nextCnt; // 넘긴횟수(next +1 prev -1)
    int totalPage; //총페이지
    String id; // 키값
    String searchNm;

    public void calculOffset(){
        this.offset = (this.curPage + (this.nextCnt-1) * this.paginationCnt - 1) * this.limit;
    }

    public void calculCurPc(){
        this.totalPage = this.totalItemCnt / this.limit;
        if (this.totalItemCnt % this.limit > 0)
            this.totalPage += 1;

        int pt = this.nextCnt;
        int pc = this.paginationCnt;
        int cpc;
        cpc = (this.totalPage >= pt * pc) ? pc : this.totalPage % pc;
        if(cpc > this.paginationCnt) cpc = this.paginationCnt;
        this.curpaginationCnt = cpc;
    }

    public void setDefault(int totalcnt){
        this.setLimit(10);
        this.setCurPage(1);
        this.setNextCnt(1);
        this.setPaginationCnt(5);
        this.setTotalItemCnt(totalcnt);
        this.calculOffset();
        this.calculCurPc();
    }
}
