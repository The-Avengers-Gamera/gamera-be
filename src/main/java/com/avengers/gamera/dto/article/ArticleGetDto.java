package com.avengers.gamera.dto.article;

import com.avengers.gamera.constant.ArticleType;
import com.avengers.gamera.entity.Game;
import com.avengers.gamera.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.OffsetDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleGetDto {
    private Long id;
    private Long gameId;
    private String gameName;
    private Long userId;
    private String userName;
    private String coverImgUrl;
    private String title;
    private String text;
    private ArticleType type;
    private OffsetDateTime createdTime;
    private OffsetDateTime updatedTime;
}
