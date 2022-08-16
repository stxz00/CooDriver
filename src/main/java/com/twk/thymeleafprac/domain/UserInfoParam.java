package com.twk.thymeleafprac.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class UserInfoParam {
    String userId;
    String userNm;
    int userAge;
    String userGender;
    String userEmail;
    String hpNo;
    String addr;
    String addrDtl;
    String profileAttachId;
    String useArtiAgrYn;
    String prslCollAgrYn;
    String mktCollAgrYn;
    String smsRcvAgrYn;
    String emailRcvAgrYn;
    Date creDt;
    Date updDt;
}
