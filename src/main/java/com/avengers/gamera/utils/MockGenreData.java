package com.avengers.gamera.utils;

import com.avengers.gamera.entity.Genre;

import java.time.OffsetDateTime;
import java.util.List;

public class MockGenreData {
    public static final Genre mockGenre1 = Genre.builder()
            .id(1L)
            .name("mockGenre1")
            .createdTime(OffsetDateTime.now())
            .updatedTime(OffsetDateTime.now())
            .build();
    public static final Genre mockGenre2 = Genre.builder()
            .id(2L)
            .name("mockGenre2")
            .createdTime(OffsetDateTime.now())
            .updatedTime(OffsetDateTime.now())
            .build();

    public static final List<Genre> mockGenreList = List.of(mockGenre1, mockGenre2);
}
