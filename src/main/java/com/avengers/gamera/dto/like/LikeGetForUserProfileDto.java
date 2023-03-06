package com.avengers.gamera.dto.like;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class LikeGetForUserProfileDto {
    private Long userId;
    private Long articleId;
    private String articleTile;
    private OffsetDateTime createdTime;
    private Boolean isArticleDeleted;
}
