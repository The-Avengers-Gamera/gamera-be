package com.avengers.gamera.dto.user;

import com.avengers.gamera.entity.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserInfoDto {
    private long id;
    private Set<Authority> authorities;
    private String email;
    private String profileImgUrl;
}