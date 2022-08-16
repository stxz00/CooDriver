package com.twk.thymeleafprac.service.common;

import com.twk.thymeleafprac.domain.*;

import java.util.HashMap;
import java.util.List;

public interface CommonService {
    public List<MenuParam> getMenu(String type);
    public List<String> getMenuType();
    public List<Integer> getMenuDepth(String type);
    String selectAttchFileGrpNo();
}
