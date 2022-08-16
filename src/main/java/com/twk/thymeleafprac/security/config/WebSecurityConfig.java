package com.twk.thymeleafprac.security.config;

import com.twk.thymeleafprac.mapper.MemberUserListMapper;
import com.twk.thymeleafprac.mapper.UserMapper;
import com.twk.thymeleafprac.security.CustomAccessDeniedHandler;
import com.twk.thymeleafprac.security.CustomUserDetailsService;
import com.twk.thymeleafprac.security.Filter.JwtAuthenticationFilter;
import com.twk.thymeleafprac.security.Filter.JwtAuthorizationFilter;
import com.twk.thymeleafprac.security.SHAPasswordEncoder;
import com.twk.thymeleafprac.security.jwt.JwtTokenProvider;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MemberUserListMapper memberUserListMapper;

    // 암호화에 필요한 PasswordEncoder 를 Bean 등록합니다.
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }

    // authenticationManager를 Bean 등록합니다.
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    //JwtAuthenticationFilter 에서 attemptAuthentication의 authenticationManager.authenticate(authenticationToken); 를 하게 되면
    //아래처럼 인증매니저가 로그인 시도를 보안적으로 안전하게 할 수 있음.
    //UserDetails를 임플한 UserParam의 getPassword와 getUsername으로 설정한 컬럼값으로 확인함.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(createUserDetailsService()).passwordEncoder(createPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .httpBasic().disable() // rest api 만을 고려하여 기본 설정은 해제하겠습니다.
                .csrf().disable() // csrf 보안 토큰 disable처리.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 토큰 기반 인증이므로 세션 역시 사용하지 않습니다.
                .and()
                    .authorizeRequests() // 요청에 대한 사용권한 체크
                    .antMatchers("/list", "/board/**", "/cmCode/**",
                            "/team/{address}/main", "/team/{address}/task/**",
                            "/team/{address}/drive/**", "/team/{address}/admin/**").hasRole("USER")
                    .anyRequest().permitAll() // 그외 나머지 요청은 누구나 접근 가능
                .and()
                    .addFilterBefore(new JwtAuthorizationFilter(authenticationManager(), jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class) // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다
                    .addFilter(new JwtAuthenticationFilter(authenticationManager(), userMapper, jwtTokenProvider, memberUserListMapper))
                    .formLogin() //로그인 폼
                    .loginPage("/") //로그인 페이지를 우리가 만든 페이지로 등록한다.
                    //스프링 시큐리티가 해당 주소로 요청오는 로그인을 가로채서 대신 로그인해줌(서비스의 loadUserByName로 알아서)
                    .defaultSuccessUrl("/")
                    .failureForwardUrl("/login")
                .and()
                    .exceptionHandling()
                    .accessDeniedHandler(createAccessDeniedHandler());
    }

    @Bean
    public PasswordEncoder createPasswordEncoder() {
        return new SHAPasswordEncoder();
    }

    @Bean
    public UserDetailsService createUserDetailsService() {
        return new CustomUserDetailsService(userMapper);
    }

    @Bean
    public AccessDeniedHandler createAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
