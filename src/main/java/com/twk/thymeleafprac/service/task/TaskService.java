package com.twk.thymeleafprac.service.task;

import com.twk.thymeleafprac.domain.*;

import java.util.HashMap;
import java.util.List;

public interface TaskService {
    public List<HashMap<String,Object>> getTaskList(String key);
    public List<HashMap<String,Object>> getUserTaskList(String key, String userId);
    public int insertTask(TaskParam param);
    public TaskParam selectTask(String key);
    public int selectBoardCnt(String key);
    public int selectCommentCnt(String key);
    public int insertTaskUserList(TaskUserListParam param);
    public List<HashMap<String, Object>> getTaskAdminData(String taskKey);
    public int selectAdminCnt(String taskKey);
    public int selectUserCnt(String taskKey);
    public int updateTaskNm(String taskKey, String taskNm);
    public int updateTaskUserPosition(String taskKey, String user);
    public List<HashMap<String, Object>> selectTaskUserList(String taskKey);
    public List<HashMap<String, Object>> selectTaskInviteList(String memberKey, String taskKey);
    public int selectTaskPos(String userId, String taskKey);
    public int deleteTask(String taskKey);
}
