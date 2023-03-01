package com.avengers.gamera.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

@Setter
@Getter
public class GameraAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final Long userId;

    public GameraAuthenticationToken(Long userId, Object principal, Object credential, Collection<? extends GrantedAuthority> authorities){
        super(principal,credential,authorities);
        this.userId=userId;
    }

    @Override
    public Map<String,Long> getDetails(){
        return Map.of("UserId", userId);
    }
}
