package com.twk.thymeleafprac.mapper;

import com.twk.thymeleafprac.domain.PageParam;
import com.twk.thymeleafprac.domain.UserParam;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper {
    public int nextval();
    public int insertUser(UserParam param);
    public List<HashMap<String,Object>> getUserList(PageParam param);
    public int getTotalCnt();
    public UserParam selectUser(String id);
    public UserParam selectUserPw(Map<String, Object> data);
    public int userYnSwitch(String userId);
}
