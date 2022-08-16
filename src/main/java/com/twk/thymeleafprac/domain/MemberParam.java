package com.twk.thymeleafprac.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class MemberParam {
    String memberKey;
    String memberNm;
    String membership;
    String memberIntro;
    String memberAddress;
    String profileAttachId;
    String contact;
    String creUserId;
    String updUserId;
    String useYn;
    Date creDt;
    Date updDt;
}
