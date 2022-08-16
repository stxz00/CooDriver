package com.twk.thymeleafprac.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class CommonCodeDtlParam {
    String cmCode;
    String cmDtlCode;
    String cmDtlCodeNm;
    String cmDtlCodeExp;
    String useYn;
    String creUserId;
    String updUserId;
    Date creDt;
    Date updDt;
}
