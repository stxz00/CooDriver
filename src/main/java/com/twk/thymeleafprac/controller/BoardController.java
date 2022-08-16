package com.twk.thymeleafprac.controller;

import com.twk.thymeleafprac.domain.*;
import com.twk.thymeleafprac.service.attch_file_grp.AttchFileGrpService;
import com.twk.thymeleafprac.service.board.BoardService;
import com.twk.thymeleafprac.service.task.TaskService;
import com.twk.thymeleafprac.service.user.UserService;
import com.twk.thymeleafprac.util.UploadFileUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Log
@Controller
@RequestMapping("/board")
public class BoardController {
    @Autowired
    BoardService boardService;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Autowired
    AttchFileGrpService attchFileGrpService;

    @GetMapping(value = "/")
    public String boardMain(Model model, @ModelAttribute PageParam pageParam){
        PageParam param = new PageParam();
        param.setDefault(boardService.getTotalCnt());

        List<HashMap<String, Object>> list = boardService.getBoardList(param);

        model.addAttribute("list", list);
        model.addAttribute("pagination", param);

        return "boardMain";
    }

    @RequestMapping("/loadList")
    public String loadList(Model model, PageParam param) throws Exception{
        param.calculOffset();
        param.calculCurPc();
        List<HashMap<String, Object>> list = boardService.getBoardList(param);

        model.addAttribute("list", list);
        model.addAttribute("pagination", param);

        return "boardMain :: #list-content";
    }

    @PostMapping(value = "/detail")
    public String boardDetail(HttpServletRequest request, Model model, String boardKey, @ModelAttribute CmtParam cmtParam){
        model.addAttribute("board", boardService.selectBoard(boardKey));
        List<CmtParam> params = boardService.getCmtList(boardKey);
        for(int i = 0; i < params.size(); i++){
            CmtParam temp = params.get(i);
            temp.setCmtLike(boardService.getLikeCnt(temp.getCmtKey()));
            FavParam param = new FavParam();
            param.setFavUser((String)request.getRemoteUser());
            param.setFavCmt(temp.getCmtKey());
            temp.setCmtCheck(boardService.getCmtCheck(param));
        }
        model.addAttribute("cmts", params);
        return "boardDetail";
    }

    @PostMapping(value = "/favToggle")
    public String favToggle(HttpServletRequest request, CmtParam param, Model model){
        FavParam fav = new FavParam();
        fav.setFavCmt(param.getCmtKey());
        fav.setFavUser((String)request.getRemoteUser());
        if(boardService.getCmtCheck(fav) > 0){
            boardService.favoriteBoardOff(fav);
        }else{
            boardService.favoriteBoardOn(fav);
        }

        List<CmtParam> params = boardService.getCmtList(param.getCmtBoard());
        for(int i = 0; i < params.size(); i++){
            CmtParam temp = params.get(i);
            fav.setFavCmt(temp.getCmtKey());
            temp.setCmtLike(boardService.getLikeCnt(temp.getCmtKey()));
            temp.setCmtCheck(boardService.getCmtCheck(fav));
        }
        model.addAttribute("cmts", params);

        return "boardDetail :: #cmt-content";
    }

    @PostMapping(value = "/commitCmt")
    public String commitCmt(HttpServletRequest request, Model model){
        String userId = request.getRemoteUser();

        String cmtkey = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        cmtkey = LocalDate.now().format(formatter) + String.format("%03d", boardService.nextvalCmt());

        CmtParam param = new CmtParam();
        param.setCmtKey(cmtkey);
        param.setCmtBoard((String)request.getParameter("cmtBoard"));
        param.setCmtContent((String)request.getParameter("cmtContent"));
        param.setCmtUser(userId);
        param.setGrpId((String)request.getParameter("grpId"));
        boardService.insertCmt(param);

        List<CmtParam> params = boardService.getCmtList(param.getCmtBoard());
        for(int i = 0; i < params.size(); i++){
            CmtParam temp = params.get(i);
            temp.setCmtLike(boardService.getLikeCnt(temp.getCmtKey()));
            FavParam favParam = new FavParam();
            favParam.setFavUser(userId);
            favParam.setFavCmt(temp.getCmtKey());
            temp.setCmtCheck(boardService.getCmtCheck(favParam));
        }

        String key = (String)request.getParameter("taskKey");
        List<BoardParam> bparams = boardService.getTaskContent(key);

        for(BoardParam bparam : bparams){
            bparam.setCmts(boardService.getCmtList(bparam.getBoardKey()));
            if ( bparam.getGrpId() != null && param.getGrpId() != "" ) {
                bparam.setAttchFiles(attchFileGrpService.getAttchFileList(bparam.getGrpId()));
            }
            for ( CmtParam cmt : bparam.getCmts() ) {
                if ( cmt.getGrpId() != null && cmt.getGrpId() != "" ) {
                    cmt.setAttchFiles(attchFileGrpService.getAttchFileList(cmt.getGrpId()));
                }
            }
        }

        List<HashMap<String,Object>> users = taskService.selectTaskUserList(key);
        model.addAttribute("users", users);
        model.addAttribute("contents", bparams);
        model.addAttribute("key", key );
        model.addAttribute("userId", userId);
        return (String)request.getParameter("url") + " :: #content-body";
    }

    @GetMapping(value = "/write")
    public String write(HttpServletRequest request, @ModelAttribute BoardParam param, Model model){
        String userId = (String)request.getRemoteUser();

        model.addAttribute("user", userService.selectUser(userId));

        return "boardWrite";
    }

    @PostMapping(value= "/insert")
    public String insertBoard(HttpServletRequest request, Model model){
        BoardParam param = new BoardParam();

        String userId = (String)request.getRemoteUser();
        param.setBoardUser(userId);

        String boardkey = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        boardkey = LocalDate.now().format(formatter) + String.format("%03d", boardService.nextval());

        param.setBoardKey(boardkey);
        param.setBoardTitle((String)request.getParameter("boardTitle"));
        param.setBoardContent((String)request.getParameter("boardContent"));
        param.setTaskKey((String)request.getParameter("taskKey"));
        param.setGrpId((String)request.getParameter("grpId"));

        boardService.insertBoard(param);

        String key = (String)request.getParameter("taskKey");
        List<BoardParam> bparams = boardService.getTaskContent(key);

        for(BoardParam bparam : bparams){
            bparam.setCmts(boardService.getCmtList(bparam.getBoardKey()));
            if ( bparam.getGrpId() != null && param.getGrpId() != "" ) {
                bparam.setAttchFiles(attchFileGrpService.getAttchFileList(bparam.getGrpId()));
            }
        }

        List<HashMap<String,Object>> users = taskService.selectTaskUserList(key);

        model.addAttribute("contents", bparams);
        model.addAttribute("key", key );
        model.addAttribute("users", users);
        model.addAttribute("userId", userId);

        return (String)request.getParameter("url") + " :: #content-body";
    }


    @PostMapping(value = "/loadOne", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity loadOne ( @RequestBody String boardKey) {

        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if("anonymousUser".equals(id)) {
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        }

        //보드 및 파일들 넣고 돌리기
        BoardParam param = boardService.selectBoard( boardKey );

        log.info(param.toString());

        param.setCmts(boardService.getCmtList(param.getBoardKey()));
        if ( param.getGrpId() != null && param.getGrpId() != "" ) {
            param.setAttchFiles(attchFileGrpService.getAttchFileList(param.getGrpId()));
        }

        return ResponseEntity.ok( param );
    }

    @PostMapping(value = "/modify", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity modifyBoard ( @RequestBody BoardParam boardParam) {

        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if("anonymousUser".equals(id)) {
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        }
        log.info(boardParam.toString());

        //보드 및 파일들 넣고 돌리기
        BoardParam param = boardService.selectBoard( boardParam.getBoardKey() );

        int result = 1;

        if (param != null) {
            result = boardService.updateBoard( boardParam );
        }

        return ResponseEntity.ok( result );
    }

    @PostMapping(value = "/cmt/loadOne", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity cmtLoadOne ( @RequestBody String cmtKey) {

        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if("anonymousUser".equals(id)) {
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        }

        //보드 및 파일들 넣고 돌리기
        CmtParam param = boardService.getCmt( cmtKey );


        if ( param.getGrpId() != null && param.getGrpId() != "" ) {
            param.setAttchFiles(attchFileGrpService.getAttchFileList(param.getGrpId()));
        }

        return ResponseEntity.ok( param );
    }

    @PostMapping(value = "/cmt/modify", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity modifyCmt ( @RequestBody CmtParam cmtParam) {

        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if("anonymousUser".equals(id)) {
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        }
        log.info(cmtParam.toString());

        //보드 및 파일들 넣고 돌리기
        CmtParam param = boardService.getCmt( cmtParam.getCmtKey() );

        int result = 1;

        if (param != null) {
            result = boardService.updateCmt( cmtParam );
        }

        return ResponseEntity.ok( result );
    }

}
