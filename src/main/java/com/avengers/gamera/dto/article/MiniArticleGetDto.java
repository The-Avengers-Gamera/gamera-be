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
public class MiniArticleGetDto {
    private Long id;
    private String coverImgUrl;
    private Game game;
    private User user;
    private String title;
    private String text;
    private ArticleType type;
//    private Long like;
//    private Long numberOfComments;
    private OffsetDateTime createdTime;
    private OffsetDateTime updatedTime;
}
