package com.twk.thymeleafprac.mapper;

import com.twk.thymeleafprac.domain.CommonCodeParam;
import com.twk.thymeleafprac.domain.PageParam;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface CommonCodeMapper {
    int insertCommonCode(CommonCodeParam commonCodeParam);
    CommonCodeParam selectCommonCode(String cmCode);
    List<HashMap<String,Object>> getCommonCodeList(PageParam param);
    int getTotalCnt();
    int updateCommonCode(CommonCodeParam commonCodeParam);
    int deleteCommonCode(String cmCode, String id);
}
