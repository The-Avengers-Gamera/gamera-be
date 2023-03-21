package com.avengers.gamera.controller;

import com.avengers.gamera.constant.EArticleType;
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
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Validated
public class ReviewController {
    private final ArticleService articleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleGetDto createReview(@RequestBody ArticlePostDto articlePostDto) {
        return articleService.createArticle(articlePostDto, EArticleType.REVIEW);
    }

    @PutMapping("/{reviewId}")
    @Operation(summary = "Update review by id")
    @ResponseStatus(HttpStatus.OK)
    public ArticleGetDto updateArticleById(@RequestBody ArticlePutDto articlePutDto, @PathVariable Long reviewId) {
        return articleService.updateArticle(articlePutDto, reviewId);
    }


    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteArticleById(@PathVariable Long reviewId) {
        return articleService.deleteArticleById(reviewId);
    }

}
