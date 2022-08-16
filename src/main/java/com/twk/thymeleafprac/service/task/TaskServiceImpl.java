package com.twk.thymeleafprac.service.task;

import com.twk.thymeleafprac.domain.*;
import com.twk.thymeleafprac.mapper.BoardMapper;
import com.twk.thymeleafprac.mapper.TaskMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@Service
@Log
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskMapper taskMapper;


    @Override
    public List<HashMap<String,Object>> getTaskList(String key){
        return taskMapper.getTaskList(key);
    }

    @Override
    public int insertTask(TaskParam param){
        String key = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        key = LocalDate.now().format(formatter) + String.format("%03d", taskMapper.nextval());
        param.setTaskKey(key);

        TaskUserListParam tulp = new TaskUserListParam();
        tulp.setPosition(1);
        tulp.setTaskKey(key);
        tulp.setUserId(param.getCreUserId());

        return insertTaskUserList(tulp) + taskMapper.insertTask(param);
    }

    @Override
    public TaskParam selectTask(String key){
        return taskMapper.selectTask(key);
    }

    @Override
    public int selectBoardCnt(String key){
        return taskMapper.selectBoardCnt(key);
    }

    @Override
    public int selectCommentCnt(String key){
        return taskMapper.selectCommentCnt(key);
    }

    @Override
    public int insertTaskUserList(TaskUserListParam param){
        return taskMapper.insertTaskUserList(param);
    }

    @Override
    public List<HashMap<String, Object>> getTaskAdminData(String taskKey){
        return taskMapper.getTaskAdminData(taskKey);
    }

    @Override
    public int selectAdminCnt(String taskKey){
        return taskMapper.selectAdminCnt(taskKey);
    }

    @Override
    public int selectUserCnt(String taskKey){
        return taskMapper.selectUserCnt(taskKey);
    }

    @Override
    public int updateTaskNm(String taskKey, String taskNm){
        HashMap<String, String> data = new HashMap<>();
        data.put("taskKey", taskKey);
        data.put("taskNm", taskNm);
        return taskMapper.updateTaskNm(data);
    }

    @Override
    public int updateTaskUserPosition(String taskKey, String user){
        HashMap<String, String> data = new HashMap<>();
        data.put("taskKey", taskKey);
        data.put("user", user);
        return taskMapper.updateTaskUserPosition(data);
    }

    @Override
    public List<HashMap<String,Object>> getUserTaskList(String key, String userId){
        HashMap<String, Object> data = new HashMap<>();
        data.put("key", key);
        data.put("userId", userId);

        return taskMapper.getUserTaskList(data);
    }

    @Override
    public List<HashMap<String, Object>> selectTaskUserList(String taskKey){
        return taskMapper.selectTaskUserList(taskKey);
    }

    @Override
    public List<HashMap<String, Object>> selectTaskInviteList(String memberKey, String taskKey){
        HashMap<String, String> data = new HashMap<>();
        data.put("memberKey", memberKey);
        data.put("taskKey", taskKey);

        return taskMapper.selectTaskInviteList(data);
    }

    @Override
    public int selectTaskPos(String userId, String taskKey){
        HashMap<String, String> data = new HashMap<>();
        data.put("userId", userId);
        data.put("taskKey", taskKey);

        return taskMapper.selectTaskPos(data);
    }

    @Override
    public int deleteTask(String taskKey){
        return taskMapper.deleteTask(taskKey);
    }
}
