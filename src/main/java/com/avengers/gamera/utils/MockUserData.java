package com.avengers.gamera.utils;

import com.avengers.gamera.dto.user.UserPostDto;

public class MockUserData {

    public static final UserPostDto mockUserPostDto = UserPostDto.builder()
            .name("weite")
            .email("weite@gmail.com")
            .password("Gamera123456")
            .build();

}
