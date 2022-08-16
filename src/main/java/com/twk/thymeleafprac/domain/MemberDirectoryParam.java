package com.twk.thymeleafprac.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class MemberDirectoryParam {
    String directoryId;
    String memberKey;
    String directoryNm;
    String visualDirectoryLocation;
    String creUserId;
    String updUserId;
    Date creDt;
    Date updDt;
}
