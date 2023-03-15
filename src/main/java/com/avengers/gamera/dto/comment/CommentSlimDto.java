package com.avengers.gamera.dto.comment;

import com.avengers.gamera.dto.user.UserSlimGetDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentSlimDto {
    private Long id;
    private String text;
    private UserSlimGetDto user;
    private OffsetDateTime createdTime;
    private OffsetDateTime updatedTime;

}
