package com.avengers.gamera.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class GameGetDto {
    private Long id;
    private String name;
    private String platform;
    private Date releaseDate;
    private String country;
    private Double scores;
    private String developers;
    private String publishers;
    private String introduction;
    private String description;
    private OffsetDateTime createdTime;
    private OffsetDateTime updatedTime;
}
