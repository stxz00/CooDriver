package com.twk.thymeleafprac.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class CmtParam {
    String cmtKey;
    String cmtBoard;
    String cmtUser;
    String cmtContent;
    String grpId;
    int cmtLike;
    int cmtCheck;
    String creDt;
    String updDt;
    List<AttchFileParam> attchFiles;
}
