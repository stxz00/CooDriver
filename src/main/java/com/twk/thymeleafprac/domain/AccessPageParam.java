package com.twk.thymeleafprac.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccessPageParam {
    String memberKey;
    String memberPath;
    String location; // memberDirectory 전용
    int count; // 다방면으로 사용할 갯수
}
