package com.twk.thymeleafprac.service.member_user_list;

import com.twk.thymeleafprac.domain.MemberUserListParam;
import com.twk.thymeleafprac.mapper.MemberMapper;
import com.twk.thymeleafprac.mapper.MemberUserListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberUserListServiceImpl implements MemberUserListService{
    @Autowired
    MemberUserListMapper memberUserListMapper;

    @Autowired
    MemberMapper memberMapper;

    @Override
    public int insertMember(MemberUserListParam param){
        return memberUserListMapper.insertMember(param);
    }

    @Override
    public MemberUserListParam selectMemberByUserId(String id) {
        return memberUserListMapper.selectMemberByUserId(id);
    }

    @Override
    public List<MemberUserListParam> selectUsers(String key){
        return memberUserListMapper.selectUsers(key);
    }

    @Override
    public String selectMemberKeyByUserId(String userId) {
        return memberUserListMapper.selectMemberKeyByUserId(userId);
    }

    @Override
    public MemberUserListParam selectMulpByUserIdAndAddr(Map<String, Object> data){
        return memberUserListMapper.selectMulpByUserIdAndAddr(data);
    }

    @Override
    public MemberUserListParam selectMulpByUserIdAndKey(Map<String, Object> data){
        return memberUserListMapper.selectMulpByUserIdAndKey(data);
    }

    @Override
    public int invUser(Map<String, Object> data){
        return memberUserListMapper.invUser(data);
    }

    @Override
    public int banUser(Map<String, Object> data) { return memberUserListMapper.banUser(data); }

    @Override
    public List<HashMap<String, Object>> selectUserForAdmin(String key){
        return memberUserListMapper.selectUserForAdmin(key);
    }

    @Override
    public List<HashMap<String, Object>> selectUserForAdminUser(String key){ return memberUserListMapper.selectUserForAdminUser(key); }

    @Override
    public List<HashMap<String, Object>> selectUserForAdminBan(String key){ return memberUserListMapper.selectUserForAdminBan(key); }

    @Override
    public List<HashMap<String, Object>> selectUserForAdminInv(String key){ return memberUserListMapper.selectUserForAdminInv(key); }

    @Override
    public void deleteMemberUser(String key, String id){
        HashMap<String, Object> data = new HashMap<>();
        data.put("key", key);
        data.put("id", id);

        memberUserListMapper.deleteMemberUser(data);
    }

    @Override
    public int posUpdate(int pos, String key, String id){
        HashMap<String, Object> data = new HashMap<>();
        data.put("pos", pos);
        data.put("key", key);
        data.put("id", id);

        return memberUserListMapper.posUpdate(data);
    }
}
