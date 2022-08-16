package com.twk.thymeleafprac.controller.util;

import com.twk.thymeleafprac.domain.AttchFileParam;
import com.twk.thymeleafprac.service.attch_file.AttchFileService;
import lombok.extern.java.Log;
import net.lingala.zip4j.io.outputstream.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log
@Controller
@RequestMapping("/file")
public class FileDownloadController {
    @Autowired
    AttchFileService attchFileService;

    @Value("${upload.drivepath}")
    private String drivePath;


    @GetMapping(value = "/download/{id}")
    public ResponseEntity downloadFile(@PathVariable("id") String id, HttpServletResponse response) throws IOException {

        InputStream in = null;
        ResponseEntity entity = null;
        if ( id != null || id != "null" ) {
            AttchFileParam loadParam = attchFileService.selectAttchFileByFileId(id);
            String downloadPath = setDownloadFilePath(loadParam.getFileUsage());
            try {
                if( loadParam != null) {
                    log.info("사용자가 업로드한 파일명=" + loadParam.getFileNm());
                    log.info("서버에 저장한 파일명=" + loadParam.getRealFileNm());

                    String serverFilePath = downloadPath + loadParam.getFilePath() + File.separator + loadParam.getRealFileNm();
                    log.info("=========================="+ serverFilePath +"======================");
                    Path source = Paths.get(serverFilePath);
                    String mimeType = Files.probeContentType(source);
                    log.info("파일 콘텐트 타입 확인 : " + mimeType);
                    String[] setmType = null;
                    MediaType mType = null;
                    if(mimeType != null) {
                        setmType = mimeType.split("/");
                        mType = new MediaType(setmType[0], setmType[1]);
                    }

//                    String fileType = loadParam.getFileType();
//                    MediaType mType = getMediaType(fileType);

                    HttpHeaders headers = new HttpHeaders();

                    in = new FileInputStream(serverFilePath);

                    BufferedInputStream bin = new BufferedInputStream(in);


                    headers.setContentType(mType);
                    headers.add("Content-Disposition", "attachment; filename=\"" + new String(loadParam.getFileNm().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + "\"");
                    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
                    headers.add("Pragma", "no-cache");
                    headers.add("Expires", "0");

                    ByteArrayOutputStream os = new ByteArrayOutputStream();


                    int bytesRead = 0, bytesBuffered = 0;

                    byte[] buffer = new byte[1024];
                    int count = 0;
                    long mem = Runtime.getRuntime().maxMemory();
                    System.out.println("Total Memory : "+Runtime.getRuntime().totalMemory());
                    System.out.println("Free Memory : "+Runtime.getRuntime().freeMemory());

                    // 자바 2기가 heap 메모리의 한계로 인해 사용하지 않음
//                    while ( ( bytesRead = bin.read(buffer) ) > -1 ) {
//
//                        os.write(buffer, 0, bytesRead);
//                        bytesBuffered += bytesRead;
//                        if ( bytesBuffered > 1024 * 1024 ) {
//                            bytesBuffered = 0;
//                            os.flush();
//                            count++;
//                            if (count > 100 ){
//                                Runtime.getRuntime().gc();
//                                long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//                                System.out.println(usedMemory + " bytes");
//                                count = 0;
//                            }
//
//                        }
//
//                    }
//                    os.flush();

                    // 대용량 파일을 다운로드 하기 위한 방법. 리소스를 던져준다.
                    Resource file = new FileSystemResource(serverFilePath);

                    entity = ResponseEntity.ok().headers(headers).body(file);
                    //entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
                }
            } catch (Exception e) {
                e.printStackTrace();
                entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
            } finally {
                if(in != null) in.close();
            }
        }

        return entity;
    }


    private String setDownloadFilePath(String path) {

        if ( path.equals("drive") ) {
            return drivePath;
        }

        return null;
    }

    private MediaType getMediaType(String formatName){
        if(formatName != null) {
            if (formatName.equals("JPG") || formatName.equals("JPEG")) {
                return MediaType.IMAGE_JPEG;
            } else if (formatName.equals("GIF")) {
                return MediaType.IMAGE_GIF;
            } else if (formatName.equals("PNG")) {
                return MediaType.IMAGE_PNG;
            } else if (formatName.equals("DOC") || formatName.equals("DOCX")) {
                return new MediaType("application", "msword");//"application/msword";
            } else if (formatName.equals("XLS") || formatName.equals("XLSX")) {
                return new MediaType("application", "vnd.ms-excel");// "application/vnd.ms-excel";
            } else if (formatName.equals("PDF")) {
                return MediaType.APPLICATION_PDF;//"application/pdf";
            } else if (formatName.equals("ZIP")) {
                return new MediaType("application", "zip"); //"application/zip";
            } else if (formatName.equals("HWP")) {
                return new MediaType("application","x-hwp");
            } else if (formatName.equals("HWT")) {
                return new MediaType("application", "x-hwt");
            } else if (formatName.equals("HML") || formatName.equals("HWPML")) {
                return new MediaType("application", "vnd.hancom.hml");
            } else if (formatName.equals("HWPX")) {
                return new MediaType("application", "vnd.hancom.hwpx");
            } else {
                return new MediaType("application", "octet-stream");
            }
        }
        return null;
    }
}
