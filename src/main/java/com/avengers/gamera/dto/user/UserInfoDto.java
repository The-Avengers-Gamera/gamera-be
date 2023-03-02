package com.avengers.gamera.dto.user;

import com.avengers.gamera.dto.authority.AuthorityGetDto;
import com.avengers.gamera.dto.authority.AuthoritySlimDto;
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
    private Set<AuthorityGetDto> authorities;
    private String email;
}