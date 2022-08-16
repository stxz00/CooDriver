package com.twk.thymeleafprac.service.common_code;


import com.twk.thymeleafprac.domain.CommonCodeDtlParam;
import com.twk.thymeleafprac.domain.PageParam;

import java.util.HashMap;
import java.util.List;

public interface CommonCodeDtlService {
    int insertCommonCodeDtl(CommonCodeDtlParam commonCodeParam);
    CommonCodeDtlParam selectCommonCodeDtl(String cmCode);
    List<HashMap<String, Object>> getCommonCodeDtlList(PageParam param);
    int getTotalCnt();
    int deleteCmCodeDtl(String cmDtlCode, String id);
    int updateCommonCodeDtl(CommonCodeDtlParam commonCodeDtlParam);
}
