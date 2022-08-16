package com.twk.thymeleafprac.service.attch_file_grp;

import com.twk.thymeleafprac.domain.AttchFileGrpParam;
import com.twk.thymeleafprac.domain.AttchFileParam;
import com.twk.thymeleafprac.domain.util.UtilParam;
import com.twk.thymeleafprac.mapper.AttchFileGrpMapper;
import com.twk.thymeleafprac.mapper.CommonMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Map;

@Log
@Service
public class AttchFileGrpServiceImpl implements AttchFileGrpService {
    @Autowired
    AttchFileGrpMapper attchFileGrpMapper;

    @Autowired
    CommonMapper commonMapper;

    @Override
    public List<AttchFileGrpParam> selectAttchFileGrpList(String grpId) {
        return attchFileGrpMapper.selectAttchFileGrpList(grpId);
    }

    @Override
    public List<AttchFileParam> selectAttchFileListByGrpId(String grpId) {
        return attchFileGrpMapper.selectAttchFileListByGrpId(grpId);
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public String insertAttchFileGrp(String[] list, String id, String grpId) {
        try {
            AttchFileGrpParam param = new AttchFileGrpParam();
            param.setGrpId(grpId);
            param.setCreUserId(id);
            param.setUpdUserId(id);

            int result = 99;

            for ( String fileId : list ) {
                param.setFileId(fileId);
                result = attchFileGrpMapper.insertAttchFileGrp(param);
                if( result != 1 ) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return null;
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return grpId;
    }

    @Override
    public List<AttchFileParam> getAttchFileList(String key) {
        return attchFileGrpMapper.getAttchFileList(key);
    }

    @Override
    public List<Map<String, Object>> selectFileJoinGrp(String grpId) {
        return attchFileGrpMapper.selectFileJoinGrp(grpId);
    }
}
