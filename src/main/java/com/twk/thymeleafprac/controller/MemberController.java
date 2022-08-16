package com.twk.thymeleafprac.controller;


import com.twk.thymeleafprac.domain.MemberParam;
import com.twk.thymeleafprac.domain.MemberUserListParam;
import com.twk.thymeleafprac.service.member.MemberService;
import com.twk.thymeleafprac.service.member_user_list.MemberUserListService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;


@Log
@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberUserListService memberUserListService;

    @PostMapping(value = "/insert", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity insertMember(@RequestBody MemberParam param) {
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if("anonymousUser".equals(id)) {
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        }

        memberService.insertMember(param, id);

        MemberUserListParam mulp = new MemberUserListParam();
        mulp.setMemberKey(param.getMemberKey());
        mulp.setUserId(id);
        mulp.setPosition(9);
        mulp.setInvYn("Y");

        memberUserListService.insertMember(mulp);

        return ResponseEntity.ok("1");
    }


}
