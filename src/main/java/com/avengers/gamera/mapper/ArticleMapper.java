package com.avengers.gamera.mapper;

import com.avengers.gamera.dto.article.ArticleGetDto;
import com.avengers.gamera.dto.article.ArticlePostDto;
import com.avengers.gamera.dto.article.MiniArticleGetDto;
import com.avengers.gamera.entity.Article;
import org.mapstruct.Mapper;

import org.mapstruct.ReportingPolicy;




@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleMapper {

    Article articlePostDtoToArticle(ArticlePostDto articlePostDto);

    ArticleGetDto articleToArticleGetDto(Article article);

    MiniArticleGetDto articleToMiniArticleGetDto(Article articles);
}
