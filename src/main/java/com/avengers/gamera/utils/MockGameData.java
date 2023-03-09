package com.avengers.gamera.utils;

import com.avengers.gamera.dto.game.GamePostDto;

public class MockGameData {
    public static final GamePostDto mockGamePostDto = GamePostDto.builder()
            .name("any")
            .country("Au")
            .build();
}
