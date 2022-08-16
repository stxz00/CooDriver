package com.twk.thymeleafprac.security.Filter;

import com.twk.thymeleafprac.domain.MemberUserListParam;
import com.twk.thymeleafprac.domain.UserParam;
import com.twk.thymeleafprac.mapper.MemberUserListMapper;
import com.twk.thymeleafprac.mapper.UserMapper;
import com.twk.thymeleafprac.security.SHAPasswordEncoder;
import com.twk.thymeleafprac.security.jwt.JwtTokenProvider;
import com.twk.thymeleafprac.service.member_user_list.MemberUserListService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

@Log
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final MemberUserListMapper memberUserListMapper;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    private String errorString;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserMapper userMapper, JwtTokenProvider jwtTokenProvider, MemberUserListMapper memberUserListMapper) {
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberUserListMapper = memberUserListMapper;
        setFilterProcessesUrl("/authenticate");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String userId = request.getParameter("userId");
        String userPw = request.getParameter("userPw");
        String addr = request.getParameter("addr");

        SHAPasswordEncoder enc = new SHAPasswordEncoder();
        String encPw = enc.encode(userPw);
        HashMap<String, Object> data = new HashMap<>();

        if(addr != null){
            //addr을 통해 memberkey를 가져오고
            //id, pw를 통해 user를 가져오고
            //user와 memberKey로 MemberUserList에서 컬럼을 검색한다
            data.put("addr", addr);
            data.put("id", userId);
            data.put("pw", encPw);

            MemberUserListParam param = memberUserListMapper.selectMulpByUserIdAndAddr(data);

            if(param == null){
                //null 이면 Exception 띄우기
                this.errorString = "ErrorNF";
                throw new BadCredentialsException("계정 없음");
            }else{
                if(param.getInvYn() == "N"){
                    this.errorString = "ErrorInv";
                    throw new BadCredentialsException("승인되지 않은 계정입니다.");
                }else if(param.getUseYn() == "N"){
                    this.errorString = "ErrorUse";
                    throw new BadCredentialsException("비활성화된 계정입니다.");
                }else{
                    Authentication authenticationToken = new UsernamePasswordAuthenticationToken(userId,userPw);
                    return authenticationManager.authenticate(authenticationToken);
                }
            }
        }else{
            data.put("id", userId);
            data.put("pw", encPw);
            MemberUserListParam param = memberUserListMapper.selectMulpByUserIdAndPw(data);
            UserParam userParam = userMapper.selectUserPw(data);
            if(userParam == null){
                this.errorString = "ErrorNF";
                throw new BadCredentialsException("계정이 없습니다");
            }
            else if(param != null){
                if(param.getInvYn().equals("N")){
                    this.errorString = "ErrorInv";
                    throw new BadCredentialsException("승인되지 않은 계정입니다.");
                }else if(param.getUseYn().equals("N")){
                    this.errorString = "ErrorUse";
                    throw new BadCredentialsException("비활성화된 계정입니다.");
                }
            }
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(userId,userPw);
            log.info(authenticationToken.toString());
            return authenticationManager.authenticate(authenticationToken);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserParam userParam = (UserParam) authResult.getPrincipal();
        log.info("로그인 성공 : " + userParam.getUserId());
        userParam.setRoles(Arrays.asList(userParam.getUserRole().split("_R", 0)));
        log.info(userParam.getRoles().toString());
        String token = jwtTokenProvider.createToken(userParam.getUsername(),
                userParam.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        Cookie c = new Cookie("token", token);
        c.setHttpOnly(true);
//        c.setSecure(true);
        Long maxAge = jwtTokenProvider.getTime()/1000L;
        int ma = maxAge.intValue();
        c.setMaxAge(ma);
        response.addCookie(c);

//        HttpSession session = request.getSession();
//        session.setAttribute("token", token);
//        session.setAttribute("userId", userParam.getUserId());

        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String userId = request.getParameter("userId");
        log.info("로그인 실패 = " + userId);

        response.setHeader("errStr", this.errorString);

        super.unsuccessfulAuthentication(request, response, failed);
    }
}
