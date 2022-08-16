package com.twk.thymeleafprac.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaskUserListParam {
    String taskKey;
    String userId;
    int position;
    String creDt;
    String updDt;
}
