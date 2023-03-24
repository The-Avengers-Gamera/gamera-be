package com.avengers.gamera.dto.article;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class ArticlePutDto {
    @NotBlank(message = "Article title must be provided")
    @Size(max = 255, message = "Article title can not be more than 255 characters.")
    private String title;

    @NotBlank(message = "Article text must be provided")
    @Size(max = 6000, message = "Article text can not be more than 6000 characters.")
    private String text;
}
