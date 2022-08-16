package com.twk.thymeleafprac.mapper;

import com.twk.thymeleafprac.domain.AttchFileGrpParam;
import com.twk.thymeleafprac.domain.AttchFileParam;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AttchFileGrpMapper {
    List<AttchFileGrpParam>  selectAttchFileGrpList(String grpId);
    List<AttchFileParam>  selectAttchFileListByGrpId(String grpId);
    int insertAttchFileGrp(AttchFileGrpParam attchFileGrpParam);
    List<AttchFileParam> getAttchFileList(String key);
    int deleteAttchFileGrp( AttchFileParam param );
    List<Map<String, Object>> selectFileJoinGrp(String grpId);
}
