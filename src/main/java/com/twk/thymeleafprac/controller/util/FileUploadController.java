package com.twk.thymeleafprac.controller.util;

import com.twk.thymeleafprac.domain.AttchFileParam;
import com.twk.thymeleafprac.domain.MemberUserListParam;
import com.twk.thymeleafprac.domain.util.UtilParam;
import com.twk.thymeleafprac.service.attch_file_grp.AttchFileGrpService;
import com.twk.thymeleafprac.service.common.CommonService;
import com.twk.thymeleafprac.service.member_directory.MemberDirectoryService;
import com.twk.thymeleafprac.service.member_user_list.MemberUserListService;
import com.twk.thymeleafprac.service.attch_file.AttchFileService;
import com.twk.thymeleafprac.util.UploadFileUtils;
import lombok.extern.java.Log;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Log
@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Value("${upload.drivepath}")
    private String drivePath;

    @Autowired
    AttchFileService attchFileService;

    @Autowired
    MemberUserListService memberUserListService;

    @Autowired
    MemberDirectoryService memberDirectoryService;

    @Autowired
    AttchFileGrpService attchFileGrpService;

    @Autowired
    CommonService commonService;

    @ResponseBody
    @PostMapping(value = "/uploadFile", consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Content-Type=multipart/form-data")
    public ResponseEntity driveUploadFile(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("fileUsage") String fileUsage,
            @RequestParam("directoryId") String directoryId,
            @RequestParam("onlyDrive") String onlyDrive
    ) throws Exception {
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if("anonymousUser".equals(id)) {
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        }

        MemberUserListParam memberUserListParam = memberUserListService.selectMemberByUserId(id);

        AttchFileParam param = new AttchFileParam();
        if ( multipartFile != null && !multipartFile.isEmpty() ) {
            String path = new String();
            if ( fileUsage.equals("drive")) {
                path = drivePath;
                Long usedSize = attchFileService.selectAttchFileSizeByMemberKeyAndId(memberUserListParam.getMemberKey(), id);
                int uploadSize = (int) multipartFile.getSize();
                int totalUsualSize = 10; //10기가
                if( memberUserListParam.getPosition() == 0 ) {
                    totalUsualSize = 2; //외부인 2기가
                }
                Long expectedSize = uploadSize + usedSize;
                log.info(expectedSize / 1024 + "sss");
                log.info(totalUsualSize * 1024 * 1024 + "ddd");
                if( (expectedSize / 1024) > (totalUsualSize * 1024 * 1024)) {
                    return ResponseEntity.ok(99);
                }

                if ( directoryId.equals("main") ) {
                    directoryId = memberDirectoryService.selectMemberDirectoryIdByMemberKey(memberUserListParam.getMemberKey());
                }
            }
            param = UploadFileUtils.uploadFile(path, multipartFile, fileUsage, directoryId, id, memberUserListParam.getMemberKey(), onlyDrive);
            return ResponseEntity.ok(attchFileService.insertAttchFile(param));
        }

        return ResponseEntity.ok(99);
    }

    @ResponseBody
    @PostMapping(value = "/drive/size", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity selectAttchFileSizeByMemberKey() throws Exception {
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if("anonymousUser".equals(id)) {
            return ResponseEntity.ok(0);
        }
        MemberUserListParam memberUserListParam = memberUserListService.selectMemberByUserId(id);
        Long usedSize = attchFileService.selectAttchFileSizeByMemberKeyAndId(memberUserListParam.getMemberKey(), id);
        log.info("해당 멤버 유저가 사용한 드라이브 용량 = " + usedSize);
        
        int totalUsualSize = 10; //10기가
        if( memberUserListParam.getPosition() == 0 ) {
            totalUsualSize = 2; //외부인 2기가
        }
        log.info("해당 멤버 유저가 사용할 수 있는 총 드라이브 용량 = " + totalUsualSize + "기가");

        HashMap<String, Object> result = new HashMap<>();
        result.put("usedSize", usedSize);
        result.put("totalUsualSize", totalUsualSize);

        return ResponseEntity.ok(result);
    }

    @ResponseBody
    @PostMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteAttchFileAndRealFile(@RequestBody AttchFileParam param) throws Exception {
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if("anonymousUser".equals(id)) {
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        }
        param.setFileId(param.getFileId().replace(".", "").replace("fid-", ""));
        log.info("삭제할 파일 아이디 : " + param.getFileId());
        param = attchFileService.selectAttchFileByFileId( param.getFileId() );

        if ( param != null ) {
            // 본인이 업로드한 파일이 아닌지 검토
            if ( !param.getCreUserId().equals(id) ) {
                log.info("----------본인이 업로드한 파일이 아님----------");
                return ResponseEntity.ok( 99 );
            }

            AttchFileParam isDeleted = UploadFileUtils.deleteFile(param, setTypePath( param.getFileUsage() ) );
            int result = 99;
            if ( isDeleted != null ) {
                if ( attchFileService.deleteAttchFileAndAttchFileGrp( param ) != null ) {
                    result = 1;
                    return ResponseEntity.ok( result );
                }
            }
        }


        return  ResponseEntity.ok( 1 );
    }

    private String setTypePath(String path) {

        if ( path.equals("drive") ) {
            return drivePath;
        }

        return null;
    }

}
