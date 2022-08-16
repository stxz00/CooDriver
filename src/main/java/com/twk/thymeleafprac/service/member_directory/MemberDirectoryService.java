package com.twk.thymeleafprac.service.member_directory;

import com.twk.thymeleafprac.domain.AccessPageParam;
import com.twk.thymeleafprac.domain.MemberDirectoryParam;
import com.twk.thymeleafprac.domain.util.UtilParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MemberDirectoryService {
    List<HashMap<String, Object>> selectMemberDirectoryList(AccessPageParam param);
    MemberDirectoryParam selectMemberDirectoryById(AccessPageParam accessPageParam);
    int createMemberDirectory(MemberDirectoryParam memberDirectoryParam, String id);
    List<HashMap<String, Object>> selectMemberDirectoryByMemberPathList(AccessPageParam accessPageParam);
    String selectMemberDirectoryIdByMemberKey(String memberKey);
    List<UtilParam> allDirectoryListByDirectoryId(UtilParam utilParam);
    int deleteDirectory(MemberDirectoryParam memberDirectoryParam);
    int moveDirectoryToDirectory(String[] directoryIdList, String parentDirectoryId, String id);
    int checkDirectoryOtherUploadFilesToMove(String[] directoryIdList, String id);
}
