package com.twk.thymeleafprac.controller;

import com.twk.thymeleafprac.domain.MenuParam;
import com.twk.thymeleafprac.domain.PageParam;
import com.twk.thymeleafprac.service.common.CommonService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Log
@RestController
@Controller
public class CommonController {

    @Autowired
    CommonService commonService;

    @GetMapping(value="/getMenu", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getMenu(HttpServletRequest request, Model model) throws Exception {
        List<String> types = commonService.getMenuType();

        for(int i = 0; i < types.size(); i++){
            List<MenuParam> temp = commonService.getMenu(types.get(i));
            HashMap<Integer, MenuParam> cont = new HashMap<>();

            for (MenuParam mp : temp) {
                cont.put(mp.getMenuKey(), mp);
            }

            int index = 0;
            while(true){
                if(temp.size() <= index) break;
                MenuParam item = temp.get(index);

                if(!request.isUserInRole("ROLE_" + item.getMenuRole()) && !item.getMenuRole().equals("USER")){
                    temp.remove(index);
                }

                else if(item.getMenuParent() > 0){
                    if(cont.get(item.getMenuParent()).getChildMenu() == null)
                        cont.get(item.getMenuParent()).setChildMenu(new ArrayList<MenuParam>());
                    cont.get(item.getMenuParent()).getChildMenu().add(item);
                    temp.remove(index);
                }else index++;
            }
            model.addAttribute(types.get(i), temp);
        }

        return ResponseEntity.ok(model);
    }
}
