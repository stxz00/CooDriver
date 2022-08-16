package com.twk.thymeleafprac.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class MenuParam {
    int menuKey;
    String menuType;
    String menuNm;
    String menuUrl;
    int sortNo;
    int collapseYn;
    int menuDepth;
    int menuParent;
    String menuRole;

    ArrayList<MenuParam> childMenu;
}
