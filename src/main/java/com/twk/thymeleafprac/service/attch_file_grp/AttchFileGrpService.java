package com.twk.thymeleafprac.service.attch_file_grp;

import com.twk.thymeleafprac.domain.AttchFileGrpParam;
import com.twk.thymeleafprac.domain.AttchFileParam;
import com.twk.thymeleafprac.domain.util.UtilParam;

import java.util.List;
import java.util.Map;

public interface AttchFileGrpService {
    List<AttchFileGrpParam> selectAttchFileGrpList(String grpId);
    List<AttchFileParam>  selectAttchFileListByGrpId(String grpId);
    String insertAttchFileGrp(String[] list, String id, String grpId);
    List<AttchFileParam> getAttchFileList(String key);
    List<Map<String, Object>> selectFileJoinGrp(String grpId);
}
