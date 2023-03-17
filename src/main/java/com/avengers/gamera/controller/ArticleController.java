package com.avengers.gamera.controller;

import com.avengers.gamera.constant.EArticleType;
import com.avengers.gamera.dto.PagingDto;
import com.avengers.gamera.dto.article.ArticleGetDto;
import com.avengers.gamera.dto.article.MiniArticleGetDto;
import com.avengers.gamera.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
@Validated
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    public ArticleGetDto getArticleById(@PathVariable Long articleId) {
        return articleService.getArticleById(articleId);
    }

    @GetMapping("/news")
    public PagingDto<List<MiniArticleGetDto>> getNews(@RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int size,@RequestParam() String platform, @RequestParam() String genre) {
        return articleService.getArticlePage(EArticleType.NEWS, page, size, platform, genre);
    }

    @GetMapping("/reviews")
    public PagingDto<List<MiniArticleGetDto>> getReviews(@RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "10") int size, @RequestParam() String platform, @RequestParam() String genre) {
        return articleService.getArticlePage(EArticleType.REVIEW, page, size, platform, genre);
    }



}
