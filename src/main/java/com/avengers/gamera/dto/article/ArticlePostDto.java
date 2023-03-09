package com.avengers.gamera.dto.article;

import com.avengers.gamera.constant.EArticleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePostDto{

    private String coverImgUrl;

    private Long gameId;

    @NotNull(message = "Author must be provided.")
    private Long authorId;

    @NotBlank(message = "Article title must be provided.")
    @Size(max = 255, message = "Article title can not be more than 255 characters.")
    private String title;

    @NotBlank(message = "Article text must be provided.")
    @Size(max = 6000, message = "Article text can not be more than 6000 characters.")
    private String text;

    @NotNull(message = "Article type must be provided.")
    @Enumerated(EnumType.STRING)
    private EArticleType type;
}
