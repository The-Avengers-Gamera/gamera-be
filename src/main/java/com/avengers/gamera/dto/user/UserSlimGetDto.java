package com.avengers.gamera.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSlimGetDto {
    private Long id;
    private String name;
}
