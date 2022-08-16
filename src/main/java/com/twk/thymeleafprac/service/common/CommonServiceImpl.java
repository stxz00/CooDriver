package com.twk.thymeleafprac.service.common;

import com.twk.thymeleafprac.domain.*;
import com.twk.thymeleafprac.mapper.CommonMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@Log
public class CommonServiceImpl implements CommonService {
    @Autowired
    CommonMapper commonMapper;

    @Override
    public List<MenuParam> getMenu(String type){
        return commonMapper.getMenu(type);
    }

    @Override
    public List<String> getMenuType(){
        return commonMapper.getMenuType();
    }

    @Override
    public List<Integer> getMenuDepth(String type){
        return commonMapper.getMenuDepth(type);
    }

    @Override
    public String selectAttchFileGrpNo() {
        return commonMapper.selectAttchFileGrpNo();
    }
}
