package com.avengers.gamera.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentPostDto {
    @NotBlank(message = "Comment text must be provided.")
    @Size(max = 800, message = "Comment text must be less than 800 characters.")
    private String text;

    private Long parentId;

    @NotNull(message = "Article must be provided.")
    private Long articleId;

    @NotNull(message = "Author must be provided.")
    private Long authorId;
}
