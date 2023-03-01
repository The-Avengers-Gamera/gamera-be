package com.avengers.gamera.auth;

import com.avengers.gamera.entity.User;
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.avengers.gamera.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameraUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("User",email));

        Set<GrantedAuthority> grantedAuthorities=user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toSet());

        return GameraUserDetails.builder()
                .id(user.getId())
                .username(user.getEmail())
                .password(user.getPassword())
                .grantedAuthorities(grantedAuthorities)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();
    }

}
