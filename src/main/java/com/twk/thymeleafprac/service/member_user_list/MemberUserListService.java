package com.twk.thymeleafprac.service.member_user_list;

import com.twk.thymeleafprac.domain.MemberUserListParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MemberUserListService {
    int insertMember(MemberUserListParam param);
    MemberUserListParam selectMemberByUserId(String id);
    List<MemberUserListParam> selectUsers(String key);
    String selectMemberKeyByUserId(String userId);
    MemberUserListParam selectMulpByUserIdAndAddr(Map<String, Object> data);
    MemberUserListParam selectMulpByUserIdAndKey(Map<String, Object> data);
    int invUser(Map<String, Object> data);
    int banUser(Map<String, Object> data);
    List<HashMap<String, Object>> selectUserForAdmin(String key);
    List<HashMap<String, Object>> selectUserForAdminUser(String key);
    List<HashMap<String, Object>> selectUserForAdminBan(String key);
    List<HashMap<String, Object>> selectUserForAdminInv(String key);
    void deleteMemberUser(String key, String id);
    int posUpdate(int pos, String key, String id);
}
