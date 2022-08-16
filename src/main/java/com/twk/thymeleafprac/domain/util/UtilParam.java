package com.twk.thymeleafprac.domain.util;

import com.twk.thymeleafprac.domain.AttchFileParam;
import com.twk.thymeleafprac.domain.MemberDirectoryParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString
public class UtilParam {
    String[] listData;
    String directoryId;
    String grpId;
    List<AttchFileParam> attchFileParamList;
    boolean isError = false;
}
