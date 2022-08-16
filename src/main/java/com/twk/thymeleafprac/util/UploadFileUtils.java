package com.twk.thymeleafprac.util;

import com.twk.thymeleafprac.domain.AttchFileParam;
import lombok.extern.java.Log;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

@Log
public class UploadFileUtils {

    @Value("${upload.drivepath}")
    private static String drivePath;

    public static AttchFileParam uploadFile(String path, MultipartFile multipartFile, String fileUsage, String directoryId, String id, String memberKey, String onlyDrive) throws Exception {
        UUID uuid = UUID.randomUUID();

        log.info("뭔데 : " + uuid.toString());

        InputStream is = multipartFile.getInputStream();

//        byte[] fileData = IOUtils.toByteArray(is);

        String fileType = multipartFile.getOriginalFilename().
                substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1).toUpperCase(Locale.ROOT);
        String realFileNm = uuid.toString() + "." + fileType;
        String savedPath = calcPath(path);

        File target = new File(path + savedPath, realFileNm);

        // 2기가 아래의 경우는 이렇게 업로드 가능하지만 그 이상의 대용량은 업로드 할 수 없으므로 아래 방식으로 채택하였다.
//        FileCopyUtils.copy(IOUtils.toByteArray(is), target);

        OutputStream os = new FileOutputStream( target );
        IOUtils.copyLarge( is, os );

        AttchFileParam attchFileParam = new AttchFileParam();
        attchFileParam.setFileId(uuid.toString());
        attchFileParam.setFileType(fileType);
        attchFileParam.setFilePath(savedPath);
        attchFileParam.setRealFileNm(realFileNm);
        attchFileParam.setFileNm(multipartFile.getOriginalFilename());
        attchFileParam.setFileSize(multipartFile.getSize());
        attchFileParam.setMemberKey(memberKey);
        attchFileParam.setFileUsage(fileUsage);
        attchFileParam.setDirectoryId(directoryId);
        attchFileParam.setCreUserId(id);
        attchFileParam.setUpdUserId(id);
        attchFileParam.setOnlyDrive(onlyDrive);

        return attchFileParam;
    }

    /* 1. 경로를 물리적으로 생성 call makeDir
     * 2. YYYY / MM/ DD 경로명을 반환  */
    private static String calcPath(String uploadPath) throws Exception {
        Calendar cal = Calendar.getInstance();
        // yearPath \2020
        String yearPath = File.separator + cal.get(Calendar.YEAR);
        // monthPath \2020\12
        String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);
        // datePath \2020\12\10
        String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));

        makeDir(uploadPath, yearPath, monthPath, datePath);
        log.info(datePath);

        return datePath;
    }

    private static void makeDir(String uploadPath, String... paths) {
        if (new File(paths[paths.length - 1]).exists()) {
            return;
        }

        for (String path : paths) {
            File dirPath = new File(uploadPath + path);

            if (!dirPath.exists()) {
                dirPath.mkdir();
            }
        }
    }

    public static AttchFileParam deleteFile(AttchFileParam param, String setTypePath) throws Exception {
        String deleteFilePath = setTypePath + param.getFilePath() + "/" + param.getRealFileNm();
        log.info( "deleteFilePath : " + deleteFilePath );
        File file = new File( deleteFilePath );
        if( file.exists() ) {
            System.gc();
            System.runFinalization();
            boolean delete = file.delete();
            if ( !delete ) {
              return null;
            }
        } else {
            return null;
        }
        return param;
    }

}
