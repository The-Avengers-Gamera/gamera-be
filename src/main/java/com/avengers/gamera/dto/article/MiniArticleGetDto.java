package com.avengers.gamera.dto.article;

import com.avengers.gamera.constant.EArticleType;
import com.avengers.gamera.dto.game.GameSlimGetDto;
import com.avengers.gamera.dto.user.UserGetDto;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MiniArticleGetDto {
    private Long id;
    private String coverImgUrl;
    private GameSlimGetDto game;
    private UserGetDto author;
    private int commentNum;
    private String title;
    private EArticleType type;
    private OffsetDateTime createdTime;
    private OffsetDateTime updatedTime;
    private int likeNum;
}
