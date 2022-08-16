package com.twk.thymeleafprac.service.attch_file;

import com.twk.thymeleafprac.domain.AttchFileParam;
import com.twk.thymeleafprac.domain.BoardParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AttchFileService {
    String insertAttchFile(AttchFileParam param);
    List<AttchFileParam> selectAttchFileListByDirectoryId(String directoryId, String key);
    AttchFileParam selectAttchFileByFileId(String id);
    List<Map<String,Object>> selectAttchFileListByMemberKeyAndFileType(String memberKey, String fileType);
    Long selectAttchFileSizeByMemberKeyAndId(String memberKey, String id);
    int updateAttchFileDirectoryId( List<AttchFileParam> list );
    AttchFileParam deleteAttchFileAndAttchFileGrp( AttchFileParam param );
    List<HashMap<String,Object>> selectAttchOnlyDriveMine(String directoryId, String memberKey, String creUserId);
    List<HashMap<String,Object>> selectAttchOnlyDriveNotMine(String directoryId, String memberKey, String creUserId);
    List<HashMap<String,Object>> selectAttchTaskBoardMine(String directoryId, String memberKey, String creUserId);
    List<HashMap<String,Object>> selectAttchTaskBoardNotMineButTaskUserList(String directoryId, String memberKey, String creUserId);
    List<HashMap<String,Object>> selectAttchTaskCmtBoardMine(String directoryId, String memberKey, String creUserId);
    List<HashMap<String,Object>> selectAttchTaskCmtBoardNotMineButTaskUserList(String directoryId, String memberKey, String creUserId);
    int checkAttchFileIsOtherUpload( List<AttchFileParam> attchFileList, String id );
}
