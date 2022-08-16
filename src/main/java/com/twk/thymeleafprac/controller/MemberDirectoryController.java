package com.twk.thymeleafprac.controller;

import com.twk.thymeleafprac.domain.MemberDirectoryParam;
import com.twk.thymeleafprac.domain.util.UtilParam;
import com.twk.thymeleafprac.service.member_directory.MemberDirectoryService;
import com.twk.thymeleafprac.service.member_user_list.MemberUserListService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log
@RestController
@RequestMapping("/directory")
public class MemberDirectoryController {
    @Autowired
    MemberDirectoryService memberDirectoryService;

    @Autowired
    MemberUserListService memberUserListService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createMemberDirectory(@RequestBody MemberDirectoryParam memberDirectoryParam) {
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if("anonymousUser".equals(id)) {
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(memberDirectoryService.createMemberDirectory(memberDirectoryParam, id));
    }

//    @PostMapping(value = "/select/one", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity selectMemberDirectoryById(@RequestBody String directoryId) {
//        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
//        if("anonymousUser".equals(id)) {
//            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
//        }
//        return ResponseEntity.ok(memberDirectoryService.selectMemberDirectoryById(directoryId));
//    }
    
    //하위 디렉토리 포함 리스트 보냄
    @PostMapping(value = "/select/allDirectoryList", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity allDirectoryListByDirectoryId(@RequestBody UtilParam utilParam) {

        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if("anonymousUser".equals(id)) {
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        }

        List<UtilParam> list = memberDirectoryService.allDirectoryListByDirectoryId(utilParam);

        // 받은 리스트의 파일리스트에서 onlyDrive 가 N 인 파일이 존재하는 경우 리턴 99 로 에러 발생
        for ( UtilParam param : list ) {
            log.info("param.isError() : " + param.isError());
            if (param.isError()) {
                log.info("-----------------------본인이 삭제할 수 없는 파일 존재-----------------------");
                return ResponseEntity.ok( 99 );
            }
        }

        Collections.reverse(list);

        return ResponseEntity.ok(list);
    }

    @PostMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteDirectory ( @RequestBody String directoryId) {

        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if("anonymousUser".equals(id)) {
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        }

        String memberKey = memberUserListService.selectMemberByUserId(id).getMemberKey();

        MemberDirectoryParam param = new MemberDirectoryParam();
        param.setMemberKey(memberKey);
        param.setDirectoryId(directoryId);

        return ResponseEntity.ok(memberDirectoryService.deleteDirectory(param));
    }

}
