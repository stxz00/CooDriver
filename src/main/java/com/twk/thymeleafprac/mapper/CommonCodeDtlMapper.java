package com.twk.thymeleafprac.mapper;

import com.twk.thymeleafprac.domain.CommonCodeDtlParam;
import com.twk.thymeleafprac.domain.PageParam;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface CommonCodeDtlMapper {
    int insertCommonCodeDtl(CommonCodeDtlParam commonCodeDtlParam);
    CommonCodeDtlParam selectCommonCode(String cmCode);
    List<HashMap<String,Object>> getCommonCodeDtlList(PageParam param);
    int getTotalCnt();
    int deleteCommonCodeDtlByCmCode(String cmCode, String id);
    int deleteCommonCodeDtl(String cmDtlCode, String id);
    int updateCommonCodeDtl(CommonCodeDtlParam commonCodeDtlParam);
}
