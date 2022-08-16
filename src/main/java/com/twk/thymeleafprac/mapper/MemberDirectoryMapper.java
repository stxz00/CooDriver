package com.twk.thymeleafprac.mapper;

import com.twk.thymeleafprac.domain.AccessPageParam;
import com.twk.thymeleafprac.domain.MemberDirectoryParam;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface MemberDirectoryMapper {
    List<HashMap<String, Object>> selectMemberDirectoryListByLocation(AccessPageParam param);
    MemberDirectoryParam selectMemberDirectoryById(String directoryId);
    int createMemberDirectory(MemberDirectoryParam memberDirectoryParam);
    List<HashMap<String, Object>> selectMemberDirectoryListByMain(AccessPageParam param);
    MemberDirectoryParam selectMemberDirectoryMain(String memberKey);
    HashMap<String, Object> selectMemberDirectoryByMemberPath(AccessPageParam accessPageParam);
    String selectMemberDirectoryIdByMemberKey(String memberKey);
    List<MemberDirectoryParam> selectMemberDirectoryListByLocationToParam(AccessPageParam param);
    int deleteDirectory(MemberDirectoryParam memberDirectoryParam);
    int moveDirectoryToDirectory(String visualDirectoryLocation, String directoryId);
}
