package com.avengers.gamera.dto.game;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GamePostDto {

    @NotBlank
    @Size(max = 255, message = "Username can not be more than 255 characters.")
    private String name;

    private String platform;

    private Date releaseDate;

    private String country;

    private Double scores;

    private String developers;

    private String publishers;

    private String introduction;

    private String description;

    private List<GameGenrePostDto> gameGenrePostDtoList;

}
