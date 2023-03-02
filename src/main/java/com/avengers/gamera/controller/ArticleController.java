package com.avengers.gamera.controller;

import com.avengers.gamera.dto.article.ArticleGetDto;
import com.avengers.gamera.dto.article.ArticlePostDto;
import com.avengers.gamera.dto.article.ArticlePutDto;
import com.avengers.gamera.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("articles")
@RequiredArgsConstructor
@Validated
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping
    @Operation(summary = "Create new article")
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleGetDto createArticle(@RequestBody ArticlePostDto articlePostDto) {
        return articleService.createArticle(articlePostDto);
    }

    @PutMapping
    @Operation(summary = "Update article by article id")
    @ResponseStatus(HttpStatus.OK)
    public ArticleGetDto updateArticle(@RequestBody ArticlePutDto articlePutDto){
        return articleService.updateArticle(articlePutDto);
    }


}
