package com.twk.thymeleafprac.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class BoardParam {
    String taskKey;
    String boardKey;
    String boardTitle;
    String boardContent;
    String grpId;
    String boardUser;
    String creDt;
    String updDt;

    List<AttchFileParam> attchFiles;
    List<CmtParam> cmts;
}
