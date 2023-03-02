package com.avengers.gamera.dto.article;

import com.avengers.gamera.constant.ArticleType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ArticlePutDto {
    @NotBlank(message = "Article id must be provided")
    private Long articleId;

    @NotBlank(message = "Article title must be provided")
    @Size(max = 255, message = "Article title can not be more than 255 characters.")
    private String title;

    @NotBlank(message = "Article text must be provided")
    @Size(max = 6000, message = "Article text can not be more than 6000 characters.")
    private String text;

    @NotBlank(message = "Article type must be provided")
    @Enumerated(EnumType.STRING)
    private ArticleType type;
}
