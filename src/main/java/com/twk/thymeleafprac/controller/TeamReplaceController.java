package com.twk.thymeleafprac.controller;

import com.twk.thymeleafprac.domain.*;
import com.twk.thymeleafprac.service.attch_file.AttchFileService;
import com.twk.thymeleafprac.service.attch_file_grp.AttchFileGrpService;
import com.twk.thymeleafprac.service.board.BoardService;
import com.twk.thymeleafprac.service.member.MemberService;
import com.twk.thymeleafprac.service.member_directory.MemberDirectoryService;
import com.twk.thymeleafprac.service.member_user_list.MemberUserListService;
import com.twk.thymeleafprac.service.task.TaskService;
import com.twk.thymeleafprac.util.UploadFileUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Log
@Controller
@RequestMapping("/replace")
public class TeamReplaceController {
    @Value("${upload.drivepath}")
    private String drivePath;

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

    @Autowired
    TaskService taskService;

    @Autowired
    BoardService boardService;

    @GetMapping(value="/{address}/drive/{directoryId}")
    public String teamMain(@PathVariable("address") String address, @PathVariable("directoryId") String directoryId, Model model) throws Exception{
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        AccessPageParam param = new AccessPageParam();
        String key = memberService.getMemberKey(address);
        param.setMemberKey(key);
        param.setMemberPath(directoryId);
        log.info("param : " + param.toString());

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

        // 4-2. 멤버 내 유저가 내가 참여한 테스크의 보드의 댓글에 업로드한 파일 리스트
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

        return "directoryMain :: #replaceSection";
    }


    @PostMapping(value="/{address}/invUser")
    public String invUser(@PathVariable("address")String address, HttpServletRequest request, Model model) throws Exception{
        String id = request.getParameter("id");
        String key = memberService.getMemberKey(address);
        String methodKey = request.getParameter("method");

        String url = null;

        HashMap<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("key", key);

        if(methodKey.equals("inv")){
            url = "navinv";
            Integer result = memberUserListService.invUser(data);
            model.addAttribute("result", result.toString());
        }else if(methodKey.equals("ban")){
            url = "navuser";
            data.put("yn", "N");
            Integer result = memberUserListService.banUser(data);
            model.addAttribute("result", result.toString());
        }else if(methodKey.equals("pardon")){
            url = "navban";
            data.put("yn", "Y");
            Integer result = memberUserListService.banUser(data);
            model.addAttribute("result", result.toString());
        }
        List<HashMap<String, Object>> list = memberUserListService.selectUserForAdmin(memberService.getMemberKey(address));
        model.addAttribute("list", list);

        return "adminMember :: #" + url;
    }

    @PostMapping(value= "/{address}/tabLoader")
    public String tabLoader(@PathVariable("address")String address, HttpServletRequest request, Model model){
        String key = memberService.getMemberKey(address);
        String tab = request.getParameter("tab");

        String url = "adminMember :: #nav" + tab;
        List<HashMap<String, Object>> list = memberUserListService.selectUserForAdmin(key);
        model.addAttribute("list", list);

        return url;
    }

    @PostMapping(value= "/{address}/rmvUser")
    public String rmvUser(@PathVariable("address")String address, HttpServletRequest request, Model model){
        String key = memberService.getMemberKey(address);
        String userId = request.getParameter("id");

        memberUserListService.deleteMemberUser(key, userId);

        List<HashMap<String, Object>> list = memberUserListService.selectUserForAdmin(key);
        model.addAttribute("list", list);

        return "adminMember :: #navban";
    }

    @PostMapping(value="/{address}/admin/task")
    public String taUpdate(@PathVariable("address")String address, Model model,
                           @RequestParam(value="users[]", required = false)List<String> users,
                           @RequestParam(value="taskKey")String taskKey,
                           @RequestParam(value="taskNm")String taskNm) throws Exception{

        taskService.updateTaskNm(taskKey, taskNm);
        if(users != null){
            for(String user : users){
                taskService.updateTaskUserPosition(taskKey, user);
            }
        }


        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String key = memberService.getMemberKey(address);
        List<HashMap<String,Object>> temp = taskService.getTaskList(key);
        for(int i = 0; i < temp.size(); i++){
            HashMap<String,Object> item = temp.get(i);
            String taskKey2 = (String)item.get("TASK_KEY");
            item.put("COMMENT_CNT", taskService.selectCommentCnt(taskKey2));
            item.put("BOARD_CNT", taskService.selectBoardCnt(taskKey2));
            item.put("ADMIN_CNT", taskService.selectAdminCnt(taskKey2));
            item.put("USER_CNT", taskService.selectUserCnt(taskKey2));
        }
        model.addAttribute("tasks", temp);

        return "adminTask :: #content-body";
    }

    @PostMapping(value="/{address}/admin/invUserTask")
    public String invUserTask(@PathVariable("address")String address, Model model,
                              @RequestParam(value="taskKey")String taskKey,
                              @RequestParam(value="users[]", required = false)List<String> users) throws Exception{
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if(users != null){
            for(String user : users){
                TaskUserListParam tulp = new TaskUserListParam();
                tulp.setUserId(user);
                tulp.setTaskKey(taskKey);
                tulp.setPosition(0);
                taskService.insertTaskUserList(tulp);
            }
        }

        List<HashMap<String,Object>> updated_users = taskService.selectTaskUserList(taskKey);
        model.addAttribute("users", updated_users);
        model.addAttribute("userId", id);

        return "taskDetail :: #taskDetailUserTable";
    }

    @PostMapping(value="/{address}/admin/removeTask")
    public String removeTask(@PathVariable("address")String address, Model model,
                             @RequestParam("taskKey")String taskKey) throws Exception{
        String memberKey = memberService.getMemberKey(address);
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        List<BoardParam> contents = boardService.getTaskContent(taskKey);
        ArrayList<String> grpKeys = new ArrayList<>();
        for(BoardParam boardParam : contents){
            if(boardParam.getGrpId() != null){
                if(!boardParam.getGrpId().equals("")){
                    grpKeys.add(boardParam.getGrpId());
                }
            }
            boardParam.setCmts(boardService.getCmtList(boardParam.getBoardKey()));
            for(CmtParam cmtParam : boardParam.getCmts()){
                if(!cmtParam.getGrpId().equals("") && cmtParam.getGrpId() != null){
                    grpKeys.add(cmtParam.getGrpId());
                }
            }
        }
        AttchFileParam atp = new AttchFileParam();
        for(String grpkey : grpKeys){
            List<Map<String, Object>> temp = attchFileGrpService.selectFileJoinGrp(grpkey);
            for(Map<String, Object> item : temp){
                atp.setFileId((String)item.get("FILE_ID"));
                atp.setFileUsage((String)item.get("FILE_USAGE"));
                atp.setFilePath((String)item.get("FILE_PATH"));
                atp.setRealFileNm((String)item.get("REAL_FILE_NM"));
                AttchFileParam isDeleted = UploadFileUtils.deleteFile(atp, setTypePath( atp.getFileUsage() ) );
                if(isDeleted != null){
                    attchFileService.deleteAttchFileAndAttchFileGrp( atp );
                }
            }
        }
        boardService.deleteTaskCmt(taskKey);
        boardService.deleteTaskBoard(taskKey);
        taskService.deleteTask(taskKey);

        List<HashMap<String,Object>> temp = taskService.getTaskList(memberKey);
        for(int i = 0; i < temp.size(); i++){
            HashMap<String,Object> item = temp.get(i);
            String taskKey2 = (String)item.get("TASK_KEY");
            item.put("COMMENT_CNT", taskService.selectCommentCnt(taskKey2));
            item.put("BOARD_CNT", taskService.selectBoardCnt(taskKey2));
            item.put("ADMIN_CNT", taskService.selectAdminCnt(taskKey2));
            item.put("USER_CNT", taskService.selectUserCnt(taskKey2));
        }
        model.addAttribute("tasks", temp);

        return "adminTask :: #content-body";
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
        return "fileListByType :: #content-body";
    }

    @PostMapping(value = "/{address}/removeCmt")
    public String removeCmt(@PathVariable("address")String address,
                            @RequestParam("cmtKey")String cmtKey,
                            @RequestParam("taskKey")String taskKey, Model model) throws Exception {

        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        CmtParam cmtParam = boardService.getCmt(cmtKey);

        if ( cmtParam.getGrpId() != null && cmtParam.getGrpId() != "" ) {
            cmtParam.setAttchFiles(attchFileGrpService.getAttchFileList(cmtParam.getGrpId()));
        }

        if(cmtParam.getAttchFiles() != null && cmtParam.getAttchFiles().size() > 0){
            for(int i = 0 ; i < cmtParam.getAttchFiles().size(); i++){
                AttchFileParam atp = new AttchFileParam();
                Object cmtFile = cmtParam.getAttchFiles().get(i);

                String fileId = ((AttchFileParam)cmtFile).getFileId();

                atp.setFileId(fileId.replace(".", "").replace("fid-", ""));
                atp = attchFileService.selectAttchFileByFileId( atp.getFileId() );
                AttchFileParam isDeleted = UploadFileUtils.deleteFile(atp, setTypePath( atp.getFileUsage() ) );
                if ( isDeleted != null ) {
                    log.info("isDeleted");
                    if ( attchFileService.deleteAttchFileAndAttchFileGrp( atp ) != null ) {
                    }
                }
            }
        }
        boardService.deleteCmt(cmtKey);

        List<BoardParam> params = boardService.getTaskContent(taskKey);
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
        model.addAttribute("contents", params);
        model.addAttribute("userId", id);

        return "taskDetail :: #content-board";
    }

    @PostMapping(value = "/{address}/removeBoard")
    public String removeBoard(@PathVariable("address")String address,
                            @RequestParam("boardKey")String boardKey,
                            @RequestParam("taskKey")String taskKey, Model model) throws Exception {

        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        ////

        BoardParam boardParam = boardService.selectBoard(boardKey);
        boardParam.setCmts(boardService.getCmtList(boardKey));

        for(CmtParam cmtParam : boardParam.getCmts()){
            String cmtKey = cmtParam.getCmtKey();
            if ( cmtParam.getGrpId() != null && cmtParam.getGrpId() != "" ) {
                cmtParam.setAttchFiles(attchFileGrpService.getAttchFileList(cmtParam.getGrpId()));
            }

            if(cmtParam.getAttchFiles() != null && cmtParam.getAttchFiles().size() > 0){
                for(int i = 0 ; i < cmtParam.getAttchFiles().size(); i++){
                    AttchFileParam atp = new AttchFileParam();
                    Object cmtFile = cmtParam.getAttchFiles().get(i);

                    String fileId = ((AttchFileParam)cmtFile).getFileId();

                    atp.setFileId(fileId.replace(".", "").replace("fid-", ""));
                    atp = attchFileService.selectAttchFileByFileId( atp.getFileId() );
                    AttchFileParam isDeleted = UploadFileUtils.deleteFile(atp, setTypePath( atp.getFileUsage() ) );
                    if ( isDeleted != null ) {
                        log.info("isDeleted");
                        if ( attchFileService.deleteAttchFileAndAttchFileGrp( atp ) != null ) {
                        }
                    }
                }
            }
            boardService.deleteCmt(cmtKey);
        }

        if ( boardParam.getGrpId() != null && boardParam.getGrpId() != "" ) {
            boardParam.setAttchFiles(attchFileGrpService.getAttchFileList(boardParam.getGrpId()));
        }

        if(boardParam.getAttchFiles() != null && boardParam.getAttchFiles().size() > 0){
            for(int i = 0 ; i < boardParam.getAttchFiles().size(); i++){
                AttchFileParam atp = new AttchFileParam();
                Object boardFile = boardParam.getAttchFiles().get(i);

                String fileId = ((AttchFileParam)boardFile).getFileId();

                atp.setFileId(fileId.replace(".", "").replace("fid-", ""));
                atp = attchFileService.selectAttchFileByFileId( atp.getFileId() );
                AttchFileParam isDeleted = UploadFileUtils.deleteFile(atp, setTypePath( atp.getFileUsage() ) );
                if ( isDeleted != null ) {
                    log.info("isDeleted");
                    if ( attchFileService.deleteAttchFileAndAttchFileGrp( atp ) != null ) {
                    }
                }
            }
        }

        boardService.deleteBoard(boardKey);

        ////

        List<BoardParam> params = boardService.getTaskContent(taskKey);
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
        model.addAttribute("contents", params);
        model.addAttribute("userId", id);

        return "taskDetail :: #content-board";
    }

    @PostMapping(value = "/{address}/loadBoard")
    public String removeBoard(@PathVariable("address")String address,
                              @RequestParam("taskKey")String taskKey, Model model) throws Exception {

        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        List<BoardParam> params = boardService.getTaskContent(taskKey);
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
        model.addAttribute("contents", params);
        model.addAttribute("userId", id);

        return "taskDetail :: #content-board";
    }

    private String setTypePath(String path) {

        if ( path.equals("drive") ) {
            return drivePath;
        }

        return null;
    }
}
