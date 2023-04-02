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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Validated
public class ReviewController {
    private final ArticleService articleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleGetDto createReview(@RequestBody ArticlePostDto articlePostDto) {
        return articleService.createArticle(articlePostDto, EArticleType.REVIEW, CurrentUser.getUserId());
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

    @PostMapping("/chat-gpt")
    public ArticleGetDto getChatGptResponse() {
        return articleService.createByChatGpt();
    }
}
