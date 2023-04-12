package com.avengers.gamera.mapper;

import com.avengers.gamera.dto.article.ArticleGetDto;
import com.avengers.gamera.dto.article.ArticlePostDto;
import com.avengers.gamera.dto.article.EsArticleGetDto;
import com.avengers.gamera.dto.article.MiniArticleGetDto;
import com.avengers.gamera.entity.Article;
import com.avengers.gamera.entity.EsArticle;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleMapper {
    @Mapping(source = "coverImgUrl", target = "coverImgUrl", defaultValue = "https://picsum.photos/800/400")
    Article articlePostDtoToArticle(ArticlePostDto articlePostDto);

    ArticleGetDto articleToArticleGetDto(Article article);

    MiniArticleGetDto articleToMiniArticleGetDto(Article articles);

    EsArticleGetDto esArticleToEsArticleGetDto(EsArticle esArticle);
}
