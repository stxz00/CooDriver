package com.twk.thymeleafprac.service.user;

import com.twk.thymeleafprac.domain.PageParam;
import com.twk.thymeleafprac.domain.UserInfoParam;

import java.util.HashMap;
import java.util.List;

public interface UserInfoService {
    UserInfoParam selectUserInfo(String id);
    List<HashMap<String, Object>> selectUserDataList(PageParam param);

}
