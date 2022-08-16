package com.twk.thymeleafprac.controller.util;

import com.twk.thymeleafprac.domain.util.UtilParam;
import com.twk.thymeleafprac.service.attch_file_grp.AttchFileGrpService;
import com.twk.thymeleafprac.service.common.CommonService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@Log
@RestController
@RequestMapping("/grp")
public class FileGrpController {
    @Autowired
    AttchFileGrpService attchFileGrpService;

    @Autowired
    CommonService commonService;

    @ResponseBody
    @PostMapping(value = "/insert", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity insertAttchFileGrp(@RequestBody UtilParam utilParam) {
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if("anonymousUser".equals(id)) {
            return ResponseEntity.ok(0);
        }
         log.info(utilParam.toString());

        for( String fileId : utilParam.getListData()) {
            log.info("fileId : " + fileId);
        }

        String grpId = commonService.selectAttchFileGrpNo();

        String result = attchFileGrpService.insertAttchFileGrp(utilParam.getListData(), id, grpId);

        return ResponseEntity.ok(grpId);
    }

    @ResponseBody
    @PostMapping(value = "/insertWithId", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity insertWithId(@RequestBody UtilParam utilParam) {
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if("anonymousUser".equals(id)) {
            return ResponseEntity.ok(0);
        }
        log.info( "insertWithId : " +  utilParam.toString());

        for( String fileId : utilParam.getListData()) {
            log.info("fileId : " + fileId);
        }

        String result = attchFileGrpService.insertAttchFileGrp(utilParam.getListData(), id, utilParam.getGrpId());

        return ResponseEntity.ok(result);
    }
}
