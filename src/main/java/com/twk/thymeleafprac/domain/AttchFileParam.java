package com.twk.thymeleafprac.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class AttchFileParam {
    String fileId;
    String fileType;
    String filePath;
    String realFileNm; //실제파일명
    String fileNm; //파일명(UUID)
    Long fileSize;
    String memberKey;
    String fileUsage;
    String directoryId;
    String creUserId;
    String updUserId;
    String onlyDrive; //드라이브에 직접 올린 경우
    Date creDt;
    Date updDt;
    
    //사용 편의용
    String directoryNm;
    String visualDirectoryLocation;
}
