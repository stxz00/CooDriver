package com.twk.thymeleafprac.service.user;

import com.twk.thymeleafprac.domain.PageParam;
import com.twk.thymeleafprac.domain.UserParam;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashMap;
import java.util.List;

//public interface UserService extends UserDetailsService {
public interface UserService {
    public int insertUser(HashMap<String, Object> data) throws Exception;
    public List<HashMap<String, Object>> getUserList(PageParam param);
    public int getTotalCnt();
    public UserParam selectUser(String id);
    public int userYnSwitch(String userId);
}
