package com.twk.thymeleafprac.controller;

import com.twk.thymeleafprac.domain.CommonCodeParam;
import com.twk.thymeleafprac.service.common_code.CommonCodeService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Log
@RequestMapping(value="/cmCode")
public class AdminCommonCodeController {
    @Autowired
    private CommonCodeService commonCodeService;

    @PostMapping(value = "/insert", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity insertCommonCode(@RequestBody CommonCodeParam commonCodeParam) {
        String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if("anonymousUser".equals(id)) {
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        }
        commonCodeParam.setCreUserId(id);
        commonCodeParam.setUpdUserId(id);
        log.info("insert cmCode : " + commonCodeParam.toString());
        return ResponseEntity.ok(commonCodeService.insertCommonCode(commonCodeParam));
    }

    @PostMapping(value = "/select", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity selectCommonCode(@RequestBody String cmCode) {
        log.info("select cmCode : " + cmCode);
        return ResponseEntity.ok(commonCodeService.selectCommonCode(cmCode));
    }

    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateCommonCode(@RequestBody CommonCodeParam commonCodeParam) {
        log.info("update cmCode : " + commonCodeParam.toString());
        String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commonCodeParam.setUpdUserId(id);
        return ResponseEntity.ok(commonCodeService.updateCommonCode(commonCodeParam));
    }

    @PostMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteCommonCode(@RequestBody String cmCode) {
        log.info("delete cmCode : " + cmCode);
        String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(commonCodeService.deleteCommonCode(cmCode, id));
    }


}
