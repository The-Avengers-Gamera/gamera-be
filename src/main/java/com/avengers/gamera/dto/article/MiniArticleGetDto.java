package com.avengers.gamera.dto.article;

import com.avengers.gamera.constant.ArticleType;
import com.avengers.gamera.dto.game.GameGetDto;
import com.avengers.gamera.dto.user.UserGetDto;
import com.avengers.gamera.entity.Game;
import com.avengers.gamera.entity.User;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.OffsetDateTime;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MiniArticleGetDto {
    private Long id;
    private String coverImgUrl;
    private GameGetDto game;
    private UserGetDto user;
    private String title;
    private ArticleType type;
    private OffsetDateTime createdTime;
    private OffsetDateTime updatedTime;
    private Long likeNum;
}
