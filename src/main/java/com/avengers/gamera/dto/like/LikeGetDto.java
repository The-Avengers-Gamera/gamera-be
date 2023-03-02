package com.avengers.gamera.dto.like;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class LikeGetDto {
    private Long userId;

    private Long articleId;

    private OffsetDateTime createdTime;
}
