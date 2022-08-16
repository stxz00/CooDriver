package com.twk.thymeleafprac.security.Filter;

import com.twk.thymeleafprac.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

@Log
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private JwtTokenProvider jwtTokenProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain filterChain) throws IOException, ServletException {
        // 헤더에서 JWT 를 받아옵니다.
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        log.info("토큰 : " + token);
        // 유효한 토큰인지 확인합니다.
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
//            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            Authentication authentication = getAuthentication(request, response,token);
            // SecurityContext 에 Authentication 객체를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response, String token) throws IOException {
        if (isNotEmpty(token)) {
            try {
                String secretKey = "PracticeProjectSecuritySecretKeyKimTaeWansKeyKeyKey";

                byte[] signingKey = secretKey.getBytes();

                Jws<Claims> parsedToken = Jwts.parserBuilder()
                        .setSigningKey(signingKey)
                        .build()
                        .parseClaimsJws(token);

                String username = parsedToken.getBody().getSubject();
                Date expiration = parsedToken.getBody().getExpiration();

//                log.info(parsedToken.getBody().toString());

                List<SimpleGrantedAuthority> authorities = ((List<?>) parsedToken.getBody()
                        .get("roles")).stream()
                        .map(authority -> new SimpleGrantedAuthority((String) authority))
                        .collect(Collectors.toList());

                if (isNotEmpty(username)) {
                    return new UsernamePasswordAuthenticationToken(username, null, authorities);
                }
            } catch (ExpiredJwtException exception) {
                String msg = String.format("Request to parse expired JWT : %s failed : %s", token, exception.getMessage());
                request.getSession().removeAttribute("token");
                response.sendError(5000, msg);
            } catch (UnsupportedJwtException exception) {
                String msg = String.format("Request to parse unsupported JWT :  %s failed : %s\"", token, exception.getMessage());
                response.sendError(5001, msg);
            } catch (MalformedJwtException exception) {
                String msg = String.format("Request to parse invalid JWT :  %s failed : %s\"", token, exception.getMessage());
                response.sendError(5002, msg);
            } catch (SecurityException exception) {
                String msg = String.format("Request to parse JWT with invalid signature :  %s failed : %s", token, exception.getMessage());
                response.sendError(5003, msg);
            } catch (IllegalArgumentException exception) {
                String msg = String.format("Request to parse empty or null JWT :  %s failed : %s", token, exception.getMessage());
                response.sendError(5004, msg);
            }catch(Exception e) {
                e.printStackTrace();
                SecurityContextHolder.clearContext();
            }
        }
        return null;
    }

    private boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    private boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }
}
