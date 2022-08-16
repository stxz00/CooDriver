package com.twk.thymeleafprac.controller;

import com.twk.thymeleafprac.domain.CommonCodeDtlParam;
import com.twk.thymeleafprac.service.common_code.CommonCodeDtlService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log
@RequestMapping("/cmCodeDtl")
public class AdminCommonCodeDtlController {
    @Autowired
    private CommonCodeDtlService commonCodeDtlService;

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteCommonCodeDtl(@RequestBody CommonCodeDtlParam commonCodeDtlParam) {
        log.info("deleteDtl : " + commonCodeDtlParam.toString());
        String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commonCodeDtlParam.setCreUserId(id);
        commonCodeDtlParam.setUpdUserId(id);
        return ResponseEntity.ok(commonCodeDtlService.insertCommonCodeDtl(commonCodeDtlParam));
    }

    @PostMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity editCommonCodeDtl(@RequestBody CommonCodeDtlParam commonCodeDtlParam) {
        log.info("editDtl : " + commonCodeDtlParam.toString());
        String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commonCodeDtlParam.setUpdUserId(id);
        return ResponseEntity.ok(commonCodeDtlService.updateCommonCodeDtl(commonCodeDtlParam));
    }

    @PostMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteCommonCodeDtl(@RequestBody String cmCodeDtl) {
        log.info("deleteDtl : " + cmCodeDtl);
        String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(commonCodeDtlService.deleteCmCodeDtl(cmCodeDtl, id));
    }

}
