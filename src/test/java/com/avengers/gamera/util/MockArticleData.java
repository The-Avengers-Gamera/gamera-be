package com.avengers.gamera.util;

import com.avengers.gamera.constant.EArticleType;
import com.avengers.gamera.dto.article.ArticlePostDto;
import com.avengers.gamera.dto.article.ArticlePutDto;
import com.avengers.gamera.entity.Article;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class MockArticleData {
    public static final Long articleId = 1L;
    public static final Long gameId = 1L;
    public static final Long authorId = 1L;

    public static final Article mockArticle = Article.builder().id(articleId)
            .game(MockGameData.mockGame)
            .author(MockUserData.mockUser)
            .commentList(Arrays.asList(MockCommentData.mockComment))
            .commentNum(1)
            .coverImgUrl("url")
            .title("title")
            .text("text")
            .type(EArticleType.REVIEW)
            .isDeleted(false)
            .tagList(new ArrayList<>())
            .createdTime(OffsetDateTime.now())
            .updatedTime(OffsetDateTime.now()).build();

    public static final ArticlePutDto mockArticlePutDto = ArticlePutDto.builder()
            .title("update title")
            .text("update text").build();

    public static final ArticlePostDto mockArticlePostDto = ArticlePostDto.builder()
            .coverImgUrl("url")
            .gameId(gameId)
            .authorId(authorId)
            .title("title")
            .text("text")
            .type(EArticleType.REVIEW).build();
}
