package com.avengers.gamera.dto.article;

import com.avengers.gamera.constant.EArticleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EsArticleGetDto {
    private String id;
    private String gameName;
    private String authorName;
    private String coverImgUrl;
    private String title;
    private String subtitle;
    private EArticleType type;
    private OffsetDateTime updatedTime;
}
