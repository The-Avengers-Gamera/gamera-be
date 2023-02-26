package com.avengers.gamera.dto.article;

import com.avengers.gamera.constant.ArticleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;


@Getter
@AllArgsConstructor
public class ArticlePatchDto {
    private Long id;

    @Size(max = 255, message = "Article title can not be more than 255 characters.")
    private String title;

    @Size(max = 6000, message = "Article text can not be more than 6000 characters.")
    private String text;

    @Enumerated(EnumType.STRING)
    private ArticleType type;
}
