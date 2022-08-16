package com.twk.thymeleafprac.controller;

import com.twk.thymeleafprac.domain.*;
import com.twk.thymeleafprac.service.attch_file.AttchFileService;
import com.twk.thymeleafprac.service.attch_file_grp.AttchFileGrpService;
import com.twk.thymeleafprac.service.board.BoardService;
import com.twk.thymeleafprac.service.member.MemberService;
import com.twk.thymeleafprac.service.member_user_list.MemberUserListService;
import com.twk.thymeleafprac.service.task.TaskService;
import com.twk.thymeleafprac.service.member_directory.MemberDirectoryService;
import com.twk.thymeleafprac.service.user.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@Log
@RequestMapping("/team")
public class TeamController {
    @Autowired
    BoardService boardService;

    @Autowired
    TaskService taskService;

    @Autowired
    UserService userService;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberUserListService memberUserListService;

    @Autowired
    MemberDirectoryService memberDirectoryService;

    @Autowired
    AttchFileService attchFileService;

    @Autowired
    AttchFileGrpService attchFileGrpService;

    @GetMapping(value="/{address}/main")
    public String teamMain(@PathVariable("address") String address, Model model) throws Exception{
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String key = memberService.getMemberKey(address);
        List<HashMap<String,Object>> temp = taskService.getUserTaskList(key, id);
        model.addAttribute("tasks", temp);
        HashMap<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("key", key);
        return "taskList";
    }

    @GetMapping(value="/{address}/task/{taskKey}")
    public String taskDetail(
            @PathVariable("address") String address,
            @PathVariable("taskKey") String taskKey,
            Model model) throws Exception{
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        List<BoardParam> params = boardService.getTaskContent(taskKey);
        List<HashMap<String,Object>> users = taskService.selectTaskUserList(taskKey);
        for(BoardParam param : params){
            param.setCmts(boardService.getCmtList(param.getBoardKey()));
            if ( param.getGrpId() != null && param.getGrpId() != "" ) {
                param.setAttchFiles(attchFileGrpService.getAttchFileList(param.getGrpId()));
            }

            for ( CmtParam cmt : param.getCmts() ) {
                if ( cmt.getGrpId() != null && cmt.getGrpId() != "" ) {
                    cmt.setAttchFiles(attchFileGrpService.getAttchFileList(cmt.getGrpId()));
                }
            }
        }
        model.addAttribute("taskPos", taskService.selectTaskPos(id, taskKey));
        model.addAttribute("key", taskKey);
        model.addAttribute("contents", params);
        model.addAttribute("users", users);
        model.addAttribute("userId", id);

        return "taskDetail";
    }

    @GetMapping(value="/{address}/task/newTask")
    public String newTask(@ModelAttribute TaskParam taskParam){
        return "newTask";
    }

    @GetMapping("/{address}/drive/{directoryId:.+}")
    public String driveMain(@PathVariable("address") String address, @PathVariable("directoryId") String directoryId, Model model) throws Exception{
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        log.info("주소 : " + address);
        log.info("아이디 : " + directoryId);

        AccessPageParam param = new AccessPageParam();
        String key = memberService.getMemberKey(address);
        param.setMemberKey(key);
        param.setMemberPath(directoryId);

        List<HashMap<String, Object>> directoryList = null;
        directoryList = memberDirectoryService.selectMemberDirectoryList(param);

        MemberDirectoryParam memberDirectoryParam = memberDirectoryService.selectMemberDirectoryById(param);
        log.info("directoryList : " + directoryList.toString());

        List<HashMap<String, Object>> parentDirectoryList = memberDirectoryService.selectMemberDirectoryByMemberPathList(param);

//        List<AttchFileParam> fileList = attchFileService.selectAttchFileListByDirectoryId(directoryId, key);
//        log.info( "fileList : " + fileList.toString() );

        // 1. 본인이 직접 드라이브에 업로드한 파일리스트
        List<HashMap<String, Object>> attchOnlyDriveMine =
                attchFileService.selectAttchOnlyDriveMine( directoryId, key, id );
        log.info("type1List : " + attchOnlyDriveMine.toString());

        // 2. 멤버 내 유저가 직접 드라이브에 업로드한 파일리스트
        List<HashMap<String, Object>> attchOnlyDriveNotMine =
                attchFileService.selectAttchOnlyDriveNotMine( directoryId, key, id );
        log.info("type2List : " + attchOnlyDriveNotMine.toString());

        // 3. 본인이 드라이브 외 업로드한 파일 리스트

            // 3-1. 본인이 멤버 내 보드에 업로드한 파일 리스트
            List<HashMap<String, Object>> attchTaskBoardMine =
                    attchFileService.selectAttchTaskBoardMine( directoryId, key, id );
            log.info("type3To1List : " + attchTaskBoardMine.toString());

            // 3-2. 본인이 멤버 내 보드의 댓글에 업로드한 파일 리스트
            List<HashMap<String, Object>> attchTaskCmtBoardMine =
                    attchFileService.selectAttchTaskCmtBoardMine( directoryId, key, id );
            log.info("type3To2List : " + attchTaskCmtBoardMine.toString());

        // 4. 멤버 내 유저가 드라이브 외 내가 참여된 곳(테스크 등)에서 업로드한 파일 리스트

            // 4-1. 멤버 내 유저가 내가 참여한 테스크의 보드에 업로드한 파일 리스트
            List<HashMap<String, Object>> attchTaskBoardNotMineButTaskUserList =
                    attchFileService.selectAttchTaskBoardNotMineButTaskUserList( directoryId, key, id );
            log.info("type4To1List : " + attchTaskBoardNotMineButTaskUserList.toString());

            // 4-2. 멤버 내 유저가 내가 참여한 테스크의 보드의 대슥ㄹ에 업로드한 파일 리스트
            List<HashMap<String, Object>> attchTaskCmtBoardNotMineButTaskUserList =
                    attchFileService.selectAttchTaskCmtBoardNotMineButTaskUserList( directoryId, key, id );
            log.info("type4To2List : " + attchTaskCmtBoardNotMineButTaskUserList.toString());

        model.addAttribute("directoryList", directoryList);
        model.addAttribute("directoryParam", memberDirectoryParam);
        model.addAttribute("parentDirectoryList", parentDirectoryList);
//        model.addAttribute("fileList", fileList);
        if ( parentDirectoryList.size() > 1 ) {
            model.addAttribute("prevDirectory", parentDirectoryList.get(parentDirectoryList.size() - 2));
        }
        // 1.
        model.addAttribute("type1List", attchOnlyDriveMine);

        // 2.
        model.addAttribute("type2List", attchOnlyDriveNotMine);

        // 3.
        model.addAttribute("type3To1List", attchTaskBoardMine);
        model.addAttribute("type3To2List", attchTaskCmtBoardMine);

        // 4.
        model.addAttribute("type4To1List", attchTaskBoardNotMineButTaskUserList);
        model.addAttribute("type4To2List", attchTaskCmtBoardNotMineButTaskUserList);

        return "directoryMain";
    }

    @PostMapping(value = "/{address}/task/insertTask")
    public String insertTask(@PathVariable("address")String address, HttpServletRequest request, Model model) throws Exception{
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        TaskParam param = new TaskParam();
        param.setTaskNm((String)request.getParameter("taskTitle"));
        param.setTaskIntro((String)request.getParameter("taskContent"));
        param.setUpdUserId(id);
        param.setCreUserId(id);
        param.setMemberKey(memberService.getMemberKey(address));
        taskService.insertTask(param);
        List<HashMap<String,Object>> temp = taskService.getUserTaskList(memberService.getMemberKey(address), id);
        model.addAttribute("tasks", temp);

        return "/taskList";
    }

    @GetMapping(value="/{address}/memberLogin")
    public String memberLogin(@PathVariable("address")String address, Model model, @ModelAttribute UserParam userParam){
        model.addAttribute("nm", memberService.getMemberFromAddress(address).getMemberNm());

        return "memberLogin";
    }

    @GetMapping(value="/{address}/memberJoin")
    public String memberJoin(@PathVariable("address")String address, Model model){
        model.addAttribute("member", memberService.getMemberFromAddress(address));

        return "memberJoin";
    }

    @GetMapping(value = {"/{address}/exp/{type}", "/{address}/exp/"})
    public String fileListByType(@PathVariable("address") String address, @PathVariable(value = "type", required = false) Optional<String> opType, Model model) throws Exception{
        String type = null;
        if( opType.isPresent() ) {
            type = opType.get().toUpperCase(Locale.ROOT);
        }
        String memberKey = memberService.getMemberKey(address);
        List<Map<String,Object>> list = attchFileService.selectAttchFileListByMemberKeyAndFileType(memberKey, type);
        model.addAttribute("list", list);
        return "fileListByType";
    }


    @PostMapping(value="/{address}/joinMulInsert")
    @ResponseBody
    public HashMap<String, Object> joinMulInsert(@PathVariable("address")String address, Model model, HttpServletRequest request) throws Exception{
        HashMap<String, Object> result = new HashMap<>();
        String userId = request.getParameter("userId");
        String userPw = request.getParameter("userPw");
        String userNm = request.getParameter("userNm");
        String userEmail = request.getParameter("userEmail");
        String userAge = request.getParameter("userAge");
        String userGender = request.getParameter("userGender");

        MemberUserListParam mulp = memberUserListService.selectMemberByUserId(userId);
        UserParam up = userService.selectUser(userId);

        if(mulp != null){
            String addr = memberService.getMemberAddressFromUserId(userId);
            if(addr.equals(address)){
                result.put("result", "N");
                return result;
            }
        }else{
            if(up == null){
                HashMap<String, Object> data = new HashMap<>();
                data.put("userId", userId);
                data.put("userPw", userPw);
                data.put("userEmail", userEmail);
                data.put("userGender", userGender);
                data.put("userAge", userAge);
                userService.insertUser(data);

                up = userService.selectUser(userId);
            }

            String memberKey = memberService.getMemberKey(address);

            mulp = new MemberUserListParam();
            mulp.setMemberKey(memberKey);
            mulp.setUserId(userId);
            mulp.setInvYn("N");

            memberUserListService.insertMember(mulp);
        }

        result.put("result", "Y");
        return result;
    }

    @GetMapping(value = "/{address}/admin/member")
    public String admin(@PathVariable("address")String address, Model model) throws Exception{
        String key = memberService.getMemberKey(address);
        List<HashMap<String, Object>> list = memberUserListService.selectUserForAdmin(key);
        model.addAttribute("list", list);
        return "adminMember";
    }

    @PostMapping(value = "/{address}/posUpdate")
    @ResponseBody
    public int posUpdate(@PathVariable("address")String address, Model model, HttpServletRequest request) throws Exception{
        String key = memberService.getMemberKey(address);
        Integer pos = Integer.parseInt(request.getParameter("pos"));
        String userId = request.getParameter("userId");

        int result = memberUserListService.posUpdate(pos, key, userId);

        return result;
    }

    @GetMapping(value = "/{address}/admin/task")
    public String adminTask(@PathVariable("address")String address, Model model) throws Exception{
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String key = memberService.getMemberKey(address);
        List<HashMap<String,Object>> temp = taskService.getTaskList(key);
        for(int i = 0; i < temp.size(); i++){
            HashMap<String,Object> item = temp.get(i);
            String taskKey = (String)item.get("TASK_KEY");
            item.put("COMMENT_CNT", taskService.selectCommentCnt(taskKey));
            item.put("BOARD_CNT", taskService.selectBoardCnt(taskKey));
            item.put("ADMIN_CNT", taskService.selectAdminCnt(taskKey));
            item.put("USER_CNT", taskService.selectUserCnt(taskKey));
        }
        model.addAttribute("tasks", temp);

        return "adminTask";
    }

    @PostMapping(value="/{address}/admin/taUpdate")
    @ResponseBody
    public List<HashMap<String, Object>> taUpdate(@PathVariable("address")String address, HttpServletRequest request) throws Exception{
        String taskKey = request.getParameter("taskKey");
        List<HashMap<String, Object>> result = taskService.getTaskAdminData(taskKey);

        return result;
    }

    @PostMapping(value="/{address}/admin/taskModal")
    @ResponseBody
    public List<HashMap<String, Object>> taskModal(@PathVariable("address")String address,
                                                   @RequestParam(value="taskKey")String taskKey) throws Exception{
        String memberKey = memberService.getMemberKey(address);
        List<HashMap<String, Object>> result = taskService.selectTaskInviteList(memberKey, taskKey);

        return result;
    }
}
