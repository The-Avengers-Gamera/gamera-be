package com.avengers.gamera.dto.tag;

import com.avengers.gamera.dto.article.ArticleGetDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagGetDto {
    private Long id;
    private String name;
    private OffsetDateTime createdTime;
    private OffsetDateTime updatedTime;
}
