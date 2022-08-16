package com.twk.thymeleafprac.mapper;

import com.twk.thymeleafprac.domain.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface TaskMapper {
    public List<HashMap<String,Object>> getTaskList(String key);
    public List<HashMap<String,Object>> getUserTaskList(HashMap<String, Object> data);
    public int insertTask(TaskParam param);
    public int nextval();
    public TaskParam selectTask(String key);
    public int selectBoardCnt(String key);
    public int selectCommentCnt(String key);
    public int insertTaskUserList(TaskUserListParam param);
    public List<HashMap<String, Object>> getTaskAdminData(String taskKey);
    public int selectAdminCnt(String taskKey);
    public int selectUserCnt(String taskKey);
    public int updateTaskNm(HashMap<String, String> keyAndNm);
    public int updateTaskUserPosition(HashMap<String, String> keyAndId);
    public List<HashMap<String, Object>> selectTaskUserList(String taskKey);
    public List<HashMap<String, Object>> selectTaskInviteList(HashMap<String, String> memberKeyAndTaskKey);
    public int selectTaskPos(HashMap<String, String> userIdAndTaskKey);
    public int deleteTask(String taskKey);
}
