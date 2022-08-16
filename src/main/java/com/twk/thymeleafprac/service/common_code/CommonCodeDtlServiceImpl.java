package com.twk.thymeleafprac.service.common_code;



import com.twk.thymeleafprac.domain.CommonCodeDtlParam;
import com.twk.thymeleafprac.domain.PageParam;
import com.twk.thymeleafprac.mapper.CommonCodeDtlMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.HashMap;
import java.util.List;

@Service
@Log
public class CommonCodeDtlServiceImpl implements CommonCodeDtlService {
    @Autowired
    CommonCodeDtlMapper commonCodeDtlMapper;

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public int insertCommonCodeDtl(CommonCodeDtlParam commonCodeDtlParam) {
        try {
            int insert = commonCodeDtlMapper.insertCommonCodeDtl(commonCodeDtlParam);
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
    public CommonCodeDtlParam selectCommonCodeDtl(String cmCode) {
        return commonCodeDtlMapper.selectCommonCode(cmCode);
    }

    @Override
    public List<HashMap<String, Object>> getCommonCodeDtlList(PageParam param) {
        return commonCodeDtlMapper.getCommonCodeDtlList(param);
    }

    @Override
    public int getTotalCnt() {
        return commonCodeDtlMapper.getTotalCnt();
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public int deleteCmCodeDtl(String cmDtlCode, String id) {
        try {
            int insert = commonCodeDtlMapper.deleteCommonCodeDtl(cmDtlCode, id);
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

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public int updateCommonCodeDtl(CommonCodeDtlParam commonCodeDtlParam) {
        try {
            int insert = commonCodeDtlMapper.updateCommonCodeDtl(commonCodeDtlParam);
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
}
