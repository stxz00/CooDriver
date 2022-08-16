package com.twk.thymeleafprac.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaskParam {
    String taskKey;
    String memberKey;
    String taskNm;
    String taskIntro;
    String updUserId;
    String useYn;
    int writePosition;
    int viewPosition;
    int commentPosition;
    int filePosition;
    String creUserId;
    String creDt;
    String updDt;
}
