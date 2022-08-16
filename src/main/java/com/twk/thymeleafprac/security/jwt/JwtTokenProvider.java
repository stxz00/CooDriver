package com.twk.thymeleafprac.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Log
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private String secretKey = "PracticeProjectSecuritySecretKeyKimTaeWansKeyKeyKey";

    //유효시간
    private long tokenValidTime = 30 * 60 * 1000L * 10;
    public long getTime(){
        return this.tokenValidTime;
    }
//    private final UserDetailsService userDetailsService;

    //객체 초기화, secretKey를 base64 인코딩
    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //jwt 토큰 생성
    public String createToken(String userPk, List<String> roles){
        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload에 저장되는 정보 단위
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 사용할 암호화 알고리즘과 signature에 들어갈 secret값 세팅
                .compact();
    }

    //JWT 토큰에서 인증정보 조회
//    public Authentication getAuthentication(String token){
//        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }

    //토큰에서 회원 정보 추출
    public String getUserPk(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    //Request의 header에서 token값을 가져옴. "Authentication" : "token값"
    public String resolveToken(HttpServletRequest request){
//        return (String)request.getSession().getAttribute("token");
        Cookie[] cs = request.getCookies();
        if(cs == null) return null;
        for(Cookie c : cs){
            if(c.getName().equals("token")){
                return c.getValue();
            }
        }
        return null;
    }

    //토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken){
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (Exception e){
            return false;
        }
    }
}
