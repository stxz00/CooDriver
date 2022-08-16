package com.twk.thymeleafprac.mapper;

import com.twk.thymeleafprac.domain.PageParam;
import com.twk.thymeleafprac.domain.UserInfoParam;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface UserInfoMapper {
    int insertUserInfo(UserInfoParam userInfoParam);
    UserInfoParam selectUserInfo(String id);
    List<HashMap<String, Object>> selectUserDataList(PageParam param);
}
