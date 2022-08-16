package com.twk.thymeleafprac.controller;

import com.twk.thymeleafprac.domain.AccessPageParam;
import com.twk.thymeleafprac.domain.MemberDirectoryParam;
import com.twk.thymeleafprac.service.member_directory.MemberDirectoryService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;

@Log
@Controller
@RequestMapping("/main")
public class MemberPageAccessController {

    @Autowired
    MemberDirectoryService memberDirectoryService;

//    @PostMapping(value = "/loadAccessPage")
//    public String loadAccessPage(Model model, AccessPageParam param) throws Exception{
//        log.info("loadAccessPage: " + param.toString());
//        String redirect = "redirect:/";
//        if ( param.getMemberPath().startsWith("drive") ) {
//            redirect += param.getMemberPath();
//            log.info("redirect : " + redirect);
//
//            return driveDirectoryMain(model, param);
//        }
//
//        return "memberMain";
//    }
//
//    public String driveDirectoryMain(Model model, AccessPageParam param) {
//        List<HashMap<String, Object>> directoryList = null;
//        directoryList = memberDirectoryService.selectMemberDirectoryList(param);
//
//        MemberDirectoryParam memberDirectoryParam = memberDirectoryService.selectMemberDirectoryById(param);
//        log.info("directoryList : " + directoryList.toString());
//
//        List<HashMap<String, Object>> parentDirectoryList = memberDirectoryService.selectMemberDirectoryByMemberPathList(param);
//
//        model.addAttribute("directoryList", directoryList);
//        model.addAttribute("directoryParam", memberDirectoryParam);
//        model.addAttribute("parentDirectoryList", parentDirectoryList);
//        return "directoryMain :: #replaceSection";
//    }
}
