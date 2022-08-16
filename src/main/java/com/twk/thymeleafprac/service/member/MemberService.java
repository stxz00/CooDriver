package com.twk.thymeleafprac.service.member;

import com.twk.thymeleafprac.domain.MemberParam;
import com.twk.thymeleafprac.domain.PageParam;

import java.util.HashMap;
import java.util.List;

public interface MemberService {
    int insertMember(MemberParam memberParam, String id);
    List<HashMap<String, Object>> getMemberList(PageParam param);
    int getUserTotalCnt(String id);
    String getMemberKey(String address);
    String getMemberAddressFromUserId(String id);
    MemberParam getMemberFromAddress(String address);
    String getMemberAddressFromKey(String key);
    int getMemberTotalCnt();
    int memberYnSwitch(String memberKey);
}
