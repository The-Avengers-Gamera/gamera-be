package com.avengers.gamera.dto.game;


import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameUpdateDto {

    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String platform;

    @NotNull
    private Date releaseDate;

    @NotNull
    private String country;

    private Boolean isDeleted;

    private String imgUrl;

    @NotNull
    private Double scores;

    @NotNull
    private String developers;

    @NotNull
    private String publishers;

    @NotNull
    private String introduction;

    @NotNull
    private String description;

    private List<GameGenrePostDto> gameGenrePostDtoList;

    private OffsetDateTime createdTime;

    private OffsetDateTime updatedTime;

}
