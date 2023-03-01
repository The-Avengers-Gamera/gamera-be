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
//    @Mapping(target = "text",expression = "java(article.getText.substring(0, article.getText.length() > 30 ? 30 : article.getText.length()))")
    ArticleGetDto articleToArticleGetDto(Article article);


    @Mapping(target = "gameId",source = "game.id")
    @Mapping(target = "gameName",source = "game.name")
    @Mapping(target = "userId",source = "user.id")
    @Mapping(target = "userName",source = "user.name")
    MiniArticleGetDto articleToMiniArticleGetDto(Article articles);




}
