package com.twk.thymeleafprac.mapper;

import com.twk.thymeleafprac.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommonMapper {
    public List<MenuParam> getMenu(String type);
    public List<String> getMenuType();
    public List<Integer> getMenuDepth(String type);
    String selectMemberNo();
    String selectAttchFileGrpNo();
}
