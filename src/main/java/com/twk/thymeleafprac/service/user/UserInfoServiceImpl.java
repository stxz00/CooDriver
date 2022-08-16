package com.twk.thymeleafprac.service.user;

import com.twk.thymeleafprac.domain.PageParam;
import com.twk.thymeleafprac.domain.UserInfoParam;
import com.twk.thymeleafprac.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService{

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public UserInfoParam selectUserInfo(String id) {
        return userInfoMapper.selectUserInfo(id);
    }

    @Override
    public List<HashMap<String, Object>> selectUserDataList(PageParam param){
        return userInfoMapper.selectUserDataList(param);
    }


}
