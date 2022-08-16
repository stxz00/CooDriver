package com.twk.thymeleafprac.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class AttchFileGrpParam {
    String grpId;
    String fileId;
    String creUserId;
    String updUserId;
    Date creDt;
    Date updDt;
}
