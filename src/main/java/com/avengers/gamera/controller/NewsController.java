package com.avengers.gamera.controller;

import com.avengers.gamera.dto.article.ArticleGetDto;
import com.avengers.gamera.dto.article.ArticlePostDto;
import com.avengers.gamera.dto.article.ArticlePutDto;
import com.avengers.gamera.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Validated
public class NewsController {
    private final ArticleService articleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleGetDto createNews(@RequestBody ArticlePostDto articlePostDto) {
        return articleService.createArticle(articlePostDto);
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
