package com.avengers.gamera.dto.game;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameGenrePostDto {
    private Long id;

    @NotBlank
    private String name;
}