package com.avengers.gamera.controller;

import com.avengers.gamera.constant.EArticleType;
import com.avengers.gamera.dto.article.ArticleGetDto;
import com.avengers.gamera.dto.article.ArticlePostDto;
import com.avengers.gamera.dto.article.ArticlePutDto;
import com.avengers.gamera.service.ArticleService;
import com.avengers.gamera.util.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Validated
public class NewsController {
    private final ArticleService articleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleGetDto createNews(@RequestBody ArticlePostDto articlePostDto) {
        return articleService.createArticle(articlePostDto, EArticleType.NEWS, CurrentUser.getUserId());
    }

    @PutMapping("/{newsId}")
    @Operation(summary = "Update news by article id")
    @ResponseStatus(HttpStatus.OK)
    public ArticleGetDto updateArticleById(@RequestBody ArticlePutDto articlePutDto, @PathVariable Long newsId) {
        return articleService.updateArticle(articlePutDto, newsId);
    }

    @DeleteMapping("/{newsId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteArticleById(@PathVariable Long newsId) {
        return articleService.deleteArticleById(newsId);
    }

}
