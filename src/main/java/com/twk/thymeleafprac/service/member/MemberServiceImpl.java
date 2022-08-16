package com.twk.thymeleafprac.service.member;

import com.twk.thymeleafprac.domain.MemberDirectoryParam;
import com.twk.thymeleafprac.domain.MemberParam;
import com.twk.thymeleafprac.domain.PageParam;
import com.twk.thymeleafprac.mapper.CommonMapper;
import com.twk.thymeleafprac.mapper.MemberDirectoryMapper;
import com.twk.thymeleafprac.mapper.MemberMapper;
import com.twk.thymeleafprac.util.Util;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.HashMap;
import java.util.List;

@Service
@Log
public class MemberServiceImpl implements MemberService{
    @Autowired
    MemberMapper memberMapper;

    @Autowired
    CommonMapper commonMapper;

    @Autowired
    MemberDirectoryMapper memberDirectoryMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertMember(MemberParam memberParam, String id) {
        log.info("멤버 진입");
        
        try{
            memberParam.setMemberKey(commonMapper.selectMemberNo());
            memberParam.setCreUserId(id);
            memberParam.setUpdUserId(id);

            log.info("--------------------------------------");
            log.info("param : " + memberParam.toString() + " id : " + id);
            log.info("--------------------------------------");

            if ( memberParam.getProfileAttachId() == null ) {
                memberParam.setProfileAttachId("");
            }
            int member =  memberMapper.insertMember(memberParam);

            MemberDirectoryParam memberDirectoryParam = new MemberDirectoryParam();

            String memberDirectoryId = new String();
            do {
                memberDirectoryId = Util.getRandomStringUtils(100);
            } while (memberDirectoryMapper.selectMemberDirectoryById(memberDirectoryId) != null);

            memberDirectoryParam.setDirectoryId(memberDirectoryId);
            memberDirectoryParam.setMemberKey(memberParam.getMemberKey());
            memberDirectoryParam.setDirectoryNm("drive");
            memberDirectoryParam.setVisualDirectoryLocation("drive");
            memberDirectoryParam.setCreUserId(id);
            memberDirectoryParam.setUpdUserId(id);
            int memberDirectory = memberDirectoryMapper.createMemberDirectory(memberDirectoryParam);

            if ( member == 1 && memberDirectory == 1 ) {
                return 1;
            } else {
                log.info("can't insert Member : " + memberParam.toString());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }

        } catch ( Exception e ) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }


        return 99;
    }

    @Override
    public List<HashMap<String, Object>> getMemberList(PageParam param) {
        return memberMapper.getMemberList(param);
    }

    @Override
    public int getUserTotalCnt(String id) {
        return memberMapper.getUserTotalCnt(id);
    }

    @Override
    public String getMemberKey(String address) {
        return memberMapper.getMemberKey(address);
    }

    @Override
    public String getMemberAddressFromUserId(String id){
        return memberMapper.getMemberAddressFromUserId(id);
    }

    @Override
    public MemberParam getMemberFromAddress(String address){
        return memberMapper.getMemberFromAddress(address);
    }

    @Override
    public String getMemberAddressFromKey(String key){
        return memberMapper.getMemberAddressFromKey(key);
    }

    @Override
    public int getMemberTotalCnt() { return memberMapper.getMemberTotalCnt(); }

    @Override
    public int memberYnSwitch(String memberKey) { return memberMapper.memberYnSwitch(memberKey); }
}
