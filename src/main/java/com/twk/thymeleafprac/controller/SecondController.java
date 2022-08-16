package com.twk.thymeleafprac.controller;

import com.twk.thymeleafprac.domain.BoardParam;
import com.twk.thymeleafprac.domain.PageParam;
import com.twk.thymeleafprac.domain.TaskParam;
import com.twk.thymeleafprac.domain.UserParam;
import com.twk.thymeleafprac.service.board.BoardService;
import com.twk.thymeleafprac.service.task.TaskService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Log
@Controller
public class SecondController {
    @Autowired
    TaskService taskService;
    @Autowired
    BoardService boardService;

    @RequestMapping("/")
    public String index(@ModelAttribute UserParam userParam) throws Exception {
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if("anonymousUser".equals(id)) {
            return "index";
        }
        return "redirect:/member/redirectMember";
    }

    @PostMapping(value = "/taskDetail")
    public String taskDetail(HttpServletRequest request, Model model) throws Exception{
        String key = (String)request.getParameter("key");

        model.addAttribute("task", taskService.selectTask(key));

        List<BoardParam> params = boardService.getTaskContent(key);

        for(BoardParam param : params){
            param.setCmts(boardService.getCmtList(param.getBoardKey()));
        }

        model.addAttribute("key", key);
        model.addAttribute("contents", params);

        return (String)request.getParameter("url") + " :: #content-body";
    }
}
