package com.avengers.gamera.entity;

import com.avengers.gamera.constant.EArticleType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EsArticle {
    private String id;
    private int gameId;
    private String gameName;
    private String authorId;
    private String authorName;
    private String coverImgUrl;
    private String title;
    private String subtitle;
    private String text;
    private EArticleType type;
    private int likeNum;
    private int commentNum;
    private OffsetDateTime createdTime;
    private OffsetDateTime updatedTime;
    private Boolean isDeleted;
}
