package com.avengers.gamera.dto.comment;

import com.avengers.gamera.dto.user.UserSlimGetDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentGetDto {
    private Long id;
    private String text;
    private UserSlimGetDto user;
    private CommentSlimDto parentComment;
    private List<CommentSlimDto> childComment;
    private OffsetDateTime createdTime;
    private OffsetDateTime updatedTime;
}
