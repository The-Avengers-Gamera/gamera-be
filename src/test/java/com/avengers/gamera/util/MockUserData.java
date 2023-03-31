package com.avengers.gamera.util;

import com.avengers.gamera.dto.user.UserPostDto;
import com.avengers.gamera.entity.User;

import java.time.OffsetDateTime;

public class MockUserData {

    public static final UserPostDto mockUserPostDto = UserPostDto.builder()
            .name("weite")
            .email("weite@gmail.com")
            .password("Gamera123456")
            .build();

    public static final User mockUser = User.builder()
            .id(1L)
            .name("Bob")
            .email("Bob@gmail.com")
            .createdTime(OffsetDateTime.now())
            .updatedTime(OffsetDateTime.now())
            .build();
}
