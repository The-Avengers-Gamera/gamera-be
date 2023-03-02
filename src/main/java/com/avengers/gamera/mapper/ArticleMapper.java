package com.avengers.gamera.mapper;

import com.avengers.gamera.dto.article.ArticleGetDto;
import com.avengers.gamera.dto.article.ArticlePostDto;
import com.avengers.gamera.dto.article.MiniArticleGetDto;
import com.avengers.gamera.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.mapstruct.ReportingPolicy;




@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleMapper {

    Article articlePostDtoToArticle(ArticlePostDto articlePostDto);

    @Mapping(target = "gameId",source = "game.id")
    @Mapping(target = "gameName",source = "game.name")
    @Mapping(target = "userId",source = "user.id")
    @Mapping(target = "userName",source = "user.name")
    ArticleGetDto articleToArticleGetDto(Article article);


    @Mapping(target = "gameId",source = "game.id")
    @Mapping(target = "gameName",source = "game.name")
    @Mapping(target = "userId",source = "user.id")
    @Mapping(target = "userName",source = "user.name")
    @Mapping(target = "text",expression = "java(articles.getText().substring(0, articles.getText().length()> 30? 30 : articles.getText().length()))")
    MiniArticleGetDto articleToMiniArticleGetDto(Article articles);
}
