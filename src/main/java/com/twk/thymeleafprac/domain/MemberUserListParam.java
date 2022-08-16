package com.twk.thymeleafprac.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberUserListParam {
    String memberKey;
    String userId;
    int position;
    String nickname;
    String dept;
    String rank;
    String phoneNumber;
    String businessContact;
    String message;
    String useYn;
    String invYn;
    String creDt;
    String updDt;
}
