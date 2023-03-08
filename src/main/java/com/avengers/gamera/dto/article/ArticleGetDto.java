package com.avengers.gamera.dto.article;

import com.avengers.gamera.constant.EArticleType;
import com.avengers.gamera.dto.comment.CommentGetDto;
import com.avengers.gamera.dto.game.GameSlimGetDto;
import com.avengers.gamera.dto.tag.TagGetDto;
import com.avengers.gamera.dto.tag.TagSlimDto;
import com.avengers.gamera.dto.user.UserSlimGetDto;
import com.avengers.gamera.entity.Tag;
import lombok.*;

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
    private UserSlimGetDto user;
    private List<CommentGetDto> commentList;
    private String coverImgUrl;
    private String title;
    private String text;
<<<<<<< HEAD
    private ArticleType type;
    private List<TagSlimDto> tagList;
=======
    private EArticleType type;
>>>>>>> develop
    private OffsetDateTime createdTime;
    private OffsetDateTime updatedTime;
}
