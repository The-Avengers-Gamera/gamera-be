package com.avengers.gamera.dto.comment;

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
    private long id;
    private String text;
    private CommentSlimDto parentComment;
    private List<CommentSlimDto> childComment;
    private OffsetDateTime createdTime;
    private OffsetDateTime updatedTime;
}
