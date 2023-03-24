package com.avengers.gamera.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GameGenrePostDto {
    private Long id;

    @NotBlank
    private String name;
}