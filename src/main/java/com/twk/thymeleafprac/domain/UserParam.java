package com.twk.thymeleafprac.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import lombok.extern.java.Log;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Log
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserParam implements UserDetails {
    String userKey;
    String userId;
    String userPw;
    String userEmail;
    String useYn;
    String creDt;
    String userRole;
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        log.info("보자 :" + roles.toString());
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword(){
        return userPw;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
