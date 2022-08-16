package com.twk.thymeleafprac.controller;

import com.twk.thymeleafprac.domain.*;
import com.twk.thymeleafprac.security.jwt.JwtTokenProvider;
import com.twk.thymeleafprac.service.common_code.CommonCodeDtlService;
import com.twk.thymeleafprac.service.common_code.CommonCodeService;
import com.twk.thymeleafprac.service.member.MemberService;
import com.twk.thymeleafprac.service.member_directory.MemberDirectoryService;
import com.twk.thymeleafprac.service.member_user_list.MemberUserListService;
import com.twk.thymeleafprac.service.user.UserInfoService;
import com.twk.thymeleafprac.service.user.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log
@Controller
public class HomeController {
    @Autowired
    UserService userService;

    @Autowired
    CommonCodeService commonCodeService;

    @Autowired
    CommonCodeDtlService commonCodeDtlService;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberUserListService memberUserListService;

    @Autowired
    MemberDirectoryService memberDirectoryService;

    @Autowired
    UserInfoService userInfoService;

    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String insert(HttpServletRequest request, Model model) throws Exception {
        HashMap<String, Object> data = new HashMap<>();
        data.put("userNm", request.getParameter("userNm"));
        data.put("userAge", request.getParameter("userAge"));
        data.put("userGender", request.getParameter("userGender"));
        data.put("userId", request.getParameter("userId"));
        data.put("userPw", request.getParameter("userPw"));
        data.put("userEmail", request.getParameter("userEmail"));

        int result = userService.insertUser(data);
        model.addAttribute("result", result);

        return "redirect:/";
    }

    @RequestMapping("/login")
    public String login() throws Exception {
        return "index";
    }

    @RequestMapping("/list")
    public String list(Model model, @ModelAttribute PageParam pageParam) throws Exception {
        PageParam param = new PageParam();
        param.setDefault(userService.getTotalCnt());

        List<HashMap<String, Object>> list = userService.getUserList(param);

        model.addAttribute("list", list);
        model.addAttribute("pagination", param);
        return "userList";
    }

    @RequestMapping("/loadList")
    public String loadList(Model model, PageParam param) throws Exception{
        param.calculOffset();
        param.calculCurPc();
        List<HashMap<String, Object>> list = userService.getUserList(param);

        model.addAttribute("list", list);
        model.addAttribute("pagination", param);

        return "userList :: #list-content";
    }

    @PostMapping("/checkout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        String result = "0";
        if(cookies != null)   //브라우저에서 null을 아무 값도 안 줄경우 조건문
            for(Cookie c : cookies)   //쿠키변수를 선언하고 쿠키의 개수만큼 반복
                if(c.getName().equals("token")) {   //쿠키의 값이 result와 같은 조건문
                    result = c.getValue();   //result에 쿠키 값을 저장
                    break;
                }
        Cookie resultCookie = new Cookie("token", result);   //resultCookie 생성, 생성되는 **위치가 중요!! 쿠키를 저장하기 바로전에 생성하자
        resultCookie.setMaxAge(0);   //MaxAge가 0이면 바로 쿠키를 없앤다.
        response.addCookie(resultCookie);


        SecurityContextHolder.clearContext();
        return null;
    }

    @GetMapping("/err/denied-page")
    public String deniedPage() {
        return "err/deniedPage";
    }

    @GetMapping("/cmCode/main")
    public String cmCodeMain(Model model, @ModelAttribute PageParam pageParam) throws Exception{
        PageParam param = new PageParam();
        param.setDefault(commonCodeService.getTotalCnt());
        List<HashMap<String, Object>> list = commonCodeService.getCommonCodeList(param);

        PageParam param2 = new PageParam();
        param2.setDefault(commonCodeDtlService.getTotalCnt());
        List<HashMap<String, Object>> list2 = commonCodeDtlService.getCommonCodeDtlList(param2);

        model.addAttribute("list", list);
        model.addAttribute("pagination", param);
        model.addAttribute("list2", list2);
        model.addAttribute("pagination2", param2);

        return "cmCodeMain";
    }

    @PostMapping("/cmCode/main/loadCmCodeList")
    public String loadCmCodeList(Model model, PageParam param) throws Exception{
        param.calculOffset();
        param.calculCurPc();
        List<HashMap<String, Object>> list = commonCodeService.getCommonCodeList(param);

        model.addAttribute("list", list);
        model.addAttribute("pagination", param);

        return "cmCodeMain :: #cm-code-list-content";
    }

    @PostMapping("/cmCode/main/loadCmCodeDtlList")
    public String loadCmCodeDtlList(Model model, PageParam param2) throws Exception{
        param2.calculOffset();
        param2.calculCurPc();
        List<HashMap<String, Object>> list2 = commonCodeDtlService.getCommonCodeDtlList(param2);

        model.addAttribute("list2", list2);
        model.addAttribute("pagination2", param2);

        return "cmCodeMain :: #cm-code-dtl-list-content";
    }

    @PostMapping("/cmCode/main/selectLoadCmCodeDtlList")
    public String selectLoadCmCodeDtlList(Model model, PageParam param2) throws Exception{
        PageParam newParam = new PageParam();
        newParam.setSearchNm(param2.getSearchNm());
        newParam.setDefault(commonCodeDtlService.getTotalCnt());
        List<HashMap<String, Object>> list2 = commonCodeDtlService.getCommonCodeDtlList(newParam);

        model.addAttribute("list2", list2);
        model.addAttribute("pagination2", newParam);

        return "cmCodeMain :: #cm-code-dtl-list-content";
    }

    @GetMapping("/cmCode/add")
    public String cmCodeAdd(@ModelAttribute CommonCodeParam commonCodeParam){
        return "cmCodeAdd";
    }

    @GetMapping("/cmCode/upd")
    public String cmCodeUpd(@ModelAttribute CommonCodeParam commonCodeParam) {
        return "cmCodeUpdate";
    }

    @PostMapping("/cmCode/main/replace1")
    public String cmCodeMainReplaceList1(Model model, @ModelAttribute PageParam pageParam) throws Exception{
        PageParam param = new PageParam();
        param.setDefault(commonCodeService.getTotalCnt());
        List<HashMap<String, Object>> list = commonCodeService.getCommonCodeList(param);

        model.addAttribute("list", list);
        model.addAttribute("pagination", param);

        return "cmCodeMain :: #cm-code-list-content";
    }

    @PostMapping("/cmCode/main/replace2")
    public String cmCodeMainReplaceList2(Model model, @ModelAttribute PageParam pageParam) throws Exception{
        PageParam param2 = new PageParam();
        param2.setDefault(commonCodeDtlService.getTotalCnt());
        List<HashMap<String, Object>> list2 = commonCodeDtlService.getCommonCodeDtlList(param2);

        model.addAttribute("list2", list2);
        model.addAttribute("pagination2", param2);

        return "cmCodeMain :: #cm-code-dtl-list-content";
    }

    @GetMapping("/member/join/select")
    public String memberJoinSelect() {
        return "memberSelectService";
    }
    @GetMapping("/member/join/basic")
    public String memberJoinBasic() {
        return "memberBasicJoin";
    }
    @GetMapping("/member/join/premium")
    public String memberJoinPremium() {
        return "memberPremiumJoin";
    }

    @GetMapping("/member/list")
    public String memberList(Model model, @ModelAttribute PageParam pageParam) throws Exception {
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if("anonymousUser".equals(id)) {
            return "redirect:/";
        }

        PageParam param = new PageParam();
        param.setDefault(memberService.getUserTotalCnt(id));

        List<HashMap<String, Object>> list = memberService.getMemberList(param);

        model.addAttribute("list", list);
        model.addAttribute("pagination", param);
        return "memberList";
    }

    @GetMapping("/member/redirectMember")
    public String redirectMember(HttpServletRequest request){
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if("anonymousUser".equals(id)) {
            return "redirect:/";
        }
        else if(request.isUserInRole("ROLE_ADMIN")){
            return "redirect:/admin";
        }
        MemberUserListParam param = memberUserListService.selectMemberByUserId(id);
        String add = "";

        if(param == null) return "redirect:/member/join/select";

        add = memberService.getMemberAddressFromKey(param.getMemberKey());

        return "redirect:/team/" + add + "/main";
    }

    @GetMapping("/member/main.act")
    public String memberHome() throws Exception {
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if("anonymousUser".equals(id)) {
            return "redirect:/";
        }

        return "memberMain";
    }

    @GetMapping("/drive/main")
    public String driveDirectoryMain(Model model) {
        String id = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        List<Map<String, Object>> directoryList = new ArrayList<>();
        model.addAttribute("directoryList", directoryList);

        return "directoryMain";
    }

    @GetMapping("/join")
    public String join(){
        return "join";
    }

    @PostMapping("/setLocalStorage")
    @ResponseBody
    public HashMap<String, Object> setLocalStorage(HttpServletRequest request){
        HashMap<String, Object> data = new HashMap<>();

        String userId = request.getParameter("userId");

        MemberUserListParam mulp = memberUserListService.selectMemberByUserId(userId);
        data.put("key", mulp.getMemberKey());
        data.put("addr", memberService.getMemberAddressFromKey(mulp.getMemberKey()));
        data.put("position", mulp.getPosition());

        return data;
    }

    @GetMapping("/admin")
    public String teamBoxAdmin(Model model){
        PageParam param = new PageParam();
        param.setDefault(memberService.getMemberTotalCnt());

        List<HashMap<String, Object>> list = memberService.getMemberList(param);

        model.addAttribute("list", list);
        model.addAttribute("pagination", param);

        param = new PageParam();
        param.setDefault(userService.getTotalCnt());

        list = userInfoService.selectUserDataList(param);

        model.addAttribute("users", list);
        model.addAttribute("pagination2", param);

        return "adminTeamBox";
    }

    @PostMapping(value = "/loadAdmin/{tab}")
    public String loadAdminUser(@PathVariable("tab")String tab, Model model,
                                PageParam param){
        List<HashMap<String, Object>> list;
        param.calculOffset();
        param.calculCurPc();

        if(tab.equals("member")) {
            list = memberService.getMemberList(param);
            model.addAttribute("list", list);
            model.addAttribute("pagination", param);
        }
        else if(tab.equals("user")){
            list = userInfoService.selectUserDataList(param);
            model.addAttribute("users", list);
            model.addAttribute("pagination2", param);
        }

        return "adminTeamBox :: #navtotal" + tab;
    }

    @PostMapping(value="/admin/removeUser/{userId}")
    public String removeUser(@PathVariable("userId")String userId, PageParam param, Model model){
        userService.userYnSwitch(userId);

        param.setDefault(userService.getTotalCnt());

        List<HashMap<String, Object>> list = userInfoService.selectUserDataList(param);
        model.addAttribute("users", list);
        model.addAttribute("pagination2", param);

        return "adminTeamBox :: #navtotaluser";
    }

    @PostMapping(value="/admin/removeMember/{memberKey}")
    public String removeMember(@PathVariable("memberKey")String memberKey, PageParam param, Model model){
        memberService.memberYnSwitch(memberKey);

        param.setDefault(memberService.getMemberTotalCnt());

        List<HashMap<String, Object>> list = memberService.getMemberList(param);
        model.addAttribute("list", list);
        model.addAttribute("pagination", param);

        return "adminTeamBox :: #navtotalmember";
    }

    @PostMapping(value="/duplicateCheck")
    @ResponseBody
    public String duplicateCheck(@RequestParam("userId")String userId){
        if(userService.selectUser(userId) == null) return "Y";
        return "N";
    }
}
