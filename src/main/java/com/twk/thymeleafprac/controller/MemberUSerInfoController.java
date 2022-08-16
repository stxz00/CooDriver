package com.twk.thymeleafprac.controller;

import com.twk.thymeleafprac.service.member_user_list.MemberUserListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/memberUserListInfo")
public class MemberUSerInfoController {
    @Autowired
    MemberUserListService memberUserListService;


    @PostMapping(value = "/getMemberUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getMemberUserInfo() {
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return ResponseEntity.ok(memberUserListService.selectMemberByUserId(id));
    }

}
