package com.twk.thymeleafprac.security;

import com.twk.thymeleafprac.domain.UserParam;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());

        if ( accessDeniedException instanceof AccessDeniedHandler ) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if ( authentication != null &&
                ((UserParam)authentication.getPrincipal()).getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
                request.setAttribute("msg", "접근 권한이 없습니다.");
                request.setAttribute("nextPage", "/");

            } else {
                request.setAttribute("msg", "로그인 권한이 없는 아이디입니다.");
                request.setAttribute("nextPage", "/login");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                SecurityContextHolder.clearContext();
            }
            log.info(accessDeniedException.getClass().getCanonicalName());
        }
        request.getRequestDispatcher("/err/denied-page").forward(request, response);

    }
}
