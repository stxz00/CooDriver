package com.twk.thymeleafprac.controller;

import com.twk.thymeleafprac.domain.AttchFileParam;
import com.twk.thymeleafprac.domain.MemberDirectoryParam;
import com.twk.thymeleafprac.domain.util.UtilParam;
import com.twk.thymeleafprac.service.attch_file.AttchFileService;
import com.twk.thymeleafprac.service.member_directory.MemberDirectoryService;
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

import java.util.List;

@Log
@RestController
@RequestMapping("/file")
public class AttchFileController {
    @Autowired
    AttchFileService attchFileService;

    @Autowired
    MemberDirectoryService memberDirectoryService;

    @PostMapping(value = "/move/directory", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createMemberDirectory( @RequestBody UtilParam utilParam ) {
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if("anonymousUser".equals(id)) {
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        }

        log.info( "파일 및 폴더 옮기기 : " + utilParam.toString() );

        // 파일리스트 내 다른 사용자가 업로드한 파일인 경우 99 리턴
        if ( utilParam.getAttchFileParamList() != null ) {
            if ( utilParam.getAttchFileParamList().size() != 0 ) {
                int check1 = attchFileService.checkAttchFileIsOtherUpload(utilParam.getAttchFileParamList(), id);
                if ( check1 != 1 ) {
                    log.info(" 현재 경로에서 옮길 수 없는 파일 존재");
                    return ResponseEntity.ok(99);
                }
            }
        }

        // 폴더리스트 내 다른 사용자가 업로드한 파일인 경우 99 리턴
        if ( utilParam.getListData() != null ) {
            if ( utilParam.getListData().length != 0 ) {
                int check2 = memberDirectoryService.checkDirectoryOtherUploadFilesToMove( utilParam.getListData(), id );
                if ( check2 != 1 ) {
                    log.info("선택한 폴더들 내 옮길 수 없는 파일 존재");
                    return ResponseEntity.ok(99);
                }
            }
        }


        int result1 = 1, result2 = 1;

        if ( utilParam.getAttchFileParamList() != null ) {

            if ( utilParam.getAttchFileParamList().size() != 0 ) {
                //            log.info("moveFileToDirectory : " + utilParam.getAttchFileParamList().toString());
                result1 = attchFileService.updateAttchFileDirectoryId(utilParam.getAttchFileParamList());
            }
        }

        if ( utilParam.getListData() != null ) {
            if ( utilParam.getListData().length != 0 ) {
                //            log.info( "moveDirectoryToDirectory : " + utilParam.getListData().toString() );
                result2 = memberDirectoryService.moveDirectoryToDirectory( utilParam.getListData(), utilParam.getDirectoryId(), id );
            }
        }

        log.info( "result1 : " + result1 + "/ result2 : " + result2 );

        if ( result1 == 1 && result2 == 1 ) {
            return ResponseEntity.ok(1);
        }

        return ResponseEntity.ok(99);
    }



}
