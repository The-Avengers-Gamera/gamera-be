package com.avengers.gamera.dto.article;

import com.avengers.gamera.constant.EArticleType;
import com.avengers.gamera.dto.game.GameSlimGetDto;
import com.avengers.gamera.dto.user.UserSlimGetDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MiniArticleGetDto {
    private Long id;
    private String coverImgUrl;
    private GameSlimGetDto game;
    private UserSlimGetDto author;
    private int commentNum;
    private String title;
    private EArticleType type;
    private OffsetDateTime createdTime;
    private OffsetDateTime updatedTime;
    private int like;
}
