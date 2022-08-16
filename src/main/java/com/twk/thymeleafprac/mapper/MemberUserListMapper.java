package com.twk.thymeleafprac.mapper;

import com.twk.thymeleafprac.domain.MemberUserListParam;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface MemberUserListMapper {
    String selectMemberKeyByUserId(String userId);
    int insertMember(MemberUserListParam param);
    MemberUserListParam selectMemberByUserId(String id);
    List<MemberUserListParam> selectUsers(String key);
    MemberUserListParam selectMulpByUserIdAndAddr(Map<String, Object> data);
    MemberUserListParam selectMulpByUserIdAndKey(Map<String, Object> data);
    MemberUserListParam selectMulpByUserIdAndPw(Map<String, Object> data);
    int invUser(Map<String, Object> data);
    int banUser(Map<String, Object> data);
    List<HashMap<String, Object>> selectUserForAdmin(String key);
    List<HashMap<String, Object>> selectUserForAdminUser(String key);
    List<HashMap<String, Object>> selectUserForAdminBan(String key);
    List<HashMap<String, Object>> selectUserForAdminInv(String key);
    int posUpdate(Map<String, Object> data);
    void deleteMemberUser(Map<String, Object> data);
}
