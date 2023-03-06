package com.avengers.gamera.dto.article;

import com.avengers.gamera.constant.ArticleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePostDto{

    private String coverImgUrl;

    @NotBlank()
    private Long gameId;

    @NotBlank()
    private Long authorId;

    @NotBlank(message = "Article title must be provided.")
    @Size(max = 255, message = "Article title can not be more than 255 characters.")
    private String title;

    @NotBlank(message = "Article text must be provided.")
    @Size(max = 6000, message = "Article text can not be more than 6000 characters.")
    private String text;

    @NotNull(message = "Article type must be provided.")
    @Enumerated(EnumType.STRING)
    private ArticleType type;
}
