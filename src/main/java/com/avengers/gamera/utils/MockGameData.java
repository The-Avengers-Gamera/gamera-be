package com.avengers.gamera.utils;

import com.avengers.gamera.dto.game.GamePostDto;
import com.avengers.gamera.entity.Game;

import java.time.OffsetDateTime;

public class MockGameData {
    public static final Game mockGame = Game.builder()
            .id(1L)
            .name("Game1")
            .description("Excellent game")
            .isDeleted(false)
            .createdTime(OffsetDateTime.now())
            .updatedTime(OffsetDateTime.now())
            .genreList(MockGenreData.mockGenreList)
            .build();

    public static final GamePostDto mockGamePostDto = GamePostDto.builder()
            .name("any")
            .country("Au")
            .build();
}
