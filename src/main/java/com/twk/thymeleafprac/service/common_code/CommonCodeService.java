package com.twk.thymeleafprac.service.common_code;

import com.twk.thymeleafprac.domain.CommonCodeParam;
import com.twk.thymeleafprac.domain.PageParam;

import java.util.HashMap;
import java.util.List;

public interface CommonCodeService {
    int insertCommonCode(CommonCodeParam commonCodeParam);
    CommonCodeParam selectCommonCode(String cmCode);
    List<HashMap<String, Object>> getCommonCodeList(PageParam param);
    int getTotalCnt();
    int updateCommonCode(CommonCodeParam commonCodeParam);
    int deleteCommonCode(String cmCode, String id);
}
