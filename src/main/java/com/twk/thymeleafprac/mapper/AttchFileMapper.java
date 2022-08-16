package com.twk.thymeleafprac.mapper;

import com.twk.thymeleafprac.domain.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface AttchFileMapper {
   int insertAttchFile(AttchFileParam param);
   List<AttchFileParam> selectAttchFileListByDirectoryId(String directoryId);
   List<AttchFileParam> selectAttchFileListByMemberKey(String memberKey);
   List<Map<String,Object>> selectAttchFileListByMemberKeyAndFileType(String memberKey, String fileType);
   AttchFileParam selectAttchFileByFileId(String fileId);
   List<Map<String,Object>> selectAttchFileListByMemberKeyAndPdf(String memberKey, String fileType);
   List<Map<String,Object>> selectAttchFileListByMemberKeyAndHwp(String memberKey, String fileType);
   List<Map<String,Object>> selectAttchFileListByMemberKeyAndImg(String memberKey, String fileType);
   List<Map<String,Object>> selectAttchFileListByMemberKeyAndPpt(String memberKey, String fileType);
   List<Map<String,Object>> selectAttchFileListByMemberKeyAndExcel(String memberKey, String fileType);
   List<Map<String,Object>> selectAttchFileListByMemberKeyAndDoc(String memberKey, String fileType);
   List<Map<String,Object>> selectAttchFileListByMemberKeyAndMusicVideo(String memberKey, String fileType);
   List<Map<String,Object>> selectAttchFileListByMemberKeyAndCad(String memberKey, String fileType);
   List<Map<String,Object>> selectAttchFileListByMemberKeyAndHtml(String memberKey, String fileType);
   List<Map<String,Object>> selectAttchFileListByMemberKeyAndZip(String memberKey, String fileType);
   List<AttchFileParam> selectAttchFileSizeByMemberKeyAndId(String memberKey, String id);
   int updateAttchFileDirectoryId(AttchFileParam param);
   int deleteAttchFile( AttchFileParam param );
   List<HashMap<String,Object>> selectAttchOnlyDriveMine(String directoryId, String memberKey, String creUserId);
   List<HashMap<String,Object>> selectAttchOnlyDriveNotMine(String directoryId, String memberKey, String creUserId);
   List<HashMap<String,Object>> selectAttchTaskBoardMine(String directoryId, String memberKey, String creUserId);
   List<HashMap<String,Object>> selectAttchTaskBoardNotMineButTaskUserList(String directoryId, String memberKey, String creUserId);
   List<HashMap<String,Object>> selectAttchTaskCmtBoardMine(String directoryId, String memberKey, String creUserId);
   List<HashMap<String,Object>> selectAttchTaskCmtBoardNotMineButTaskUserList(String directoryId, String memberKey, String creUserId);

}
