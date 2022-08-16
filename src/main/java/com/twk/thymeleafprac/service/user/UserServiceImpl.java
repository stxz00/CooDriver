package com.twk.thymeleafprac.service.user;

import com.twk.thymeleafprac.domain.MemberDirectoryParam;
import com.twk.thymeleafprac.domain.PageParam;
import com.twk.thymeleafprac.domain.UserInfoParam;
import com.twk.thymeleafprac.domain.UserParam;
import com.twk.thymeleafprac.mapper.MemberDirectoryMapper;
import com.twk.thymeleafprac.mapper.UserInfoMapper;
import com.twk.thymeleafprac.mapper.UserMapper;
import com.twk.thymeleafprac.security.SHAPasswordEncoder;
import com.twk.thymeleafprac.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
@Log
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserInfoMapper userInfoMapper;

    SHAPasswordEncoder shaPasswordEncoder = new SHAPasswordEncoder();

//    @Autowired
//    PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public int insertUser(HashMap<String, Object> data) throws Exception {
        try {
            String userKey = "";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            userKey = LocalDate.now().format(formatter) + String.format("%03d", userMapper.nextval());

            UserParam usertemp = UserParam.builder()
                    .userKey(userKey)
                    .userId((String)data.get("userId"))
                    .userPw(shaPasswordEncoder.encode((String)data.get("userPw")))
                    .userEmail((String)data.get("userEmail"))
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
            String role = "";
            for(int i = 0; i < usertemp.getRoles().size(); i++){
                role += usertemp.getRoles().get(i) + "_R";
            }
            String[] spl = role.split("_R", 0);

            usertemp.setUserRole(role);

            int user = userMapper.insertUser(usertemp);

            UserInfoParam userInfoParam = new UserInfoParam();

            userInfoParam.setUserId((String)data.get("userId"));
            userInfoParam.setUserNm((String)data.get("userNm"));
            userInfoParam.setUserAge(Integer.parseInt((String)data.get("userAge")));
            userInfoParam.setUserGender((String)data.get("userGender"));
            userInfoParam.setUserEmail((String)data.get("userEmail"));

            int userInfo = userInfoMapper.insertUserInfo(userInfoParam);

            if ( user == 1 && userInfo == 1 ) {
                return 1;
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return 99;
    }

    @Override
    public List<HashMap<String, Object>> getUserList(PageParam param){
        List<HashMap<String,Object>> result = userMapper.getUserList(param);
        return result;
    }

    @Override
    public int getTotalCnt(){
        return userMapper.getTotalCnt();
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserParam temp = userMapper.selectUser(username);
//        temp.setRoles(Arrays.asList(temp.getUserRole().split("_R", 0)));
//        return temp;
//    }

    @Override
    public UserParam selectUser(String id){
        return userMapper.selectUser(id);
    }

    @Override
    public int userYnSwitch(String userId){
        return userMapper.userYnSwitch(userId);
    }
}
