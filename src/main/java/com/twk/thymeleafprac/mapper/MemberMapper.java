package com.twk.thymeleafprac.mapper;

import com.twk.thymeleafprac.domain.MemberParam;
import com.twk.thymeleafprac.domain.PageParam;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface MemberMapper {
    int insertMember(MemberParam memberParam);
    List<HashMap<String, Object>> getMemberList(PageParam param);
    int getUserTotalCnt(String id);
    String getMemberKey(String address);
    String getMemberAddressFromUserId(String id);
    MemberParam getMemberFromAddress(String address);
    String getMemberAddressFromKey(String key);
    int getMemberTotalCnt();
    int memberYnSwitch(String memberKey);
}
