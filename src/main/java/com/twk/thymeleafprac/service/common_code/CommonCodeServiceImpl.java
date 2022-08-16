package com.twk.thymeleafprac.service.common_code;

import com.twk.thymeleafprac.domain.CommonCodeParam;
import com.twk.thymeleafprac.domain.PageParam;
import com.twk.thymeleafprac.mapper.CommonCodeDtlMapper;
import com.twk.thymeleafprac.mapper.CommonCodeMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.HashMap;
import java.util.List;

@Service
@Log
public class CommonCodeServiceImpl implements CommonCodeService {
    @Autowired
    CommonCodeMapper commonCodeMapper;

    @Autowired
    CommonCodeDtlMapper commonCodeDtlMapper;

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public int insertCommonCode(CommonCodeParam commonCodeParam) {
        try {
            int insert = commonCodeMapper.insertCommonCode(commonCodeParam);
            if(insert == 1) {
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
    public CommonCodeParam selectCommonCode(String cmCode) {
        return commonCodeMapper.selectCommonCode(cmCode);
    }

    @Override
    public List<HashMap<String, Object>> getCommonCodeList(PageParam param) {
        return commonCodeMapper.getCommonCodeList(param);
    }

    @Override
    public int getTotalCnt() {
        return commonCodeMapper.getTotalCnt();
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public int updateCommonCode(CommonCodeParam commonCodeParam) {
        try {
            int result = commonCodeMapper.updateCommonCode(commonCodeParam);

            if( result == 1) {
                return result;
            } else {
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();;
        }
        return 99;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public int deleteCommonCode(String cmCode, String id) {
        try{
            int result1 = commonCodeMapper.deleteCommonCode(cmCode, id);
            int result2 = commonCodeDtlMapper.deleteCommonCodeDtlByCmCode(cmCode, id);
            log.info("result1 : " + result1 + ", result12 : " + result2);
            if (result1 == 1 && result2 > -1) {
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
}
