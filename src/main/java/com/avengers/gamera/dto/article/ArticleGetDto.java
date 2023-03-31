package com.avengers.gamera.dto.article;

import com.avengers.gamera.constant.EArticleType;
import com.avengers.gamera.dto.comment.CommentGetDto;
import com.avengers.gamera.dto.game.GameSlimGetDto;
import com.avengers.gamera.dto.tag.TagSlimDto;
import com.avengers.gamera.dto.user.UserSlimGetDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleGetDto {
    private Long id;
    private GameSlimGetDto game;
    private UserSlimGetDto author;
    private List<CommentGetDto> commentList;
    private String coverImgUrl;
    private String title;
    private String text;
    private List<TagSlimDto> tagList;
    private EArticleType type;
    private OffsetDateTime createdTime;
    private OffsetDateTime updatedTime;
    private int likeNum;
    private int commentNum;
}
