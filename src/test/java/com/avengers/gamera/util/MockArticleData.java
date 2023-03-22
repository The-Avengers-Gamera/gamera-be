package com.avengers.gamera.util;

import com.avengers.gamera.constant.EArticleType;
import com.avengers.gamera.dto.article.ArticlePostDto;
import com.avengers.gamera.dto.article.ArticlePutDto;
import com.avengers.gamera.entity.Article;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public class MockArticleData {
    public static final Long articleId = 1L;
    public static final Long gameId = 1L;
    public static final Long authorId = 1L;

    public static final Article mockArticle = Article.builder().id(articleId)
            .game(MockGameData.mockGame)
            .author(MockUserData.mockUser)
            .commentList(new ArrayList<>())
            .coverImgUrl("url")
            .title("title")
            .text("text")
            .type(EArticleType.REVIEW)
            .isDeleted(false)
            .createdTime(OffsetDateTime.now())
            .updatedTime(OffsetDateTime.now()).build();

    public static final ArticlePutDto mockArticlePutDto = ArticlePutDto.builder()
            .title("update title")
            .text("update text").build();

}
