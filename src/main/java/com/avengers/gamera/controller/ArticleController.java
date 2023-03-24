package com.avengers.gamera.controller;

import com.avengers.gamera.constant.EArticleType;
import com.avengers.gamera.dto.PagingDto;
import com.avengers.gamera.dto.article.ArticleGetDto;
import com.avengers.gamera.dto.article.MiniArticleGetDto;
import com.avengers.gamera.service.ArticleService;
import com.avengers.gamera.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
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
    private final LikeService likeService;

    @GetMapping("/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    public ArticleGetDto getArticleById(@PathVariable Long articleId) {
        return articleService.getArticleById(articleId);
    }

    @GetMapping("/news")
    public PagingDto<List<MiniArticleGetDto>> getNews(@RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(defaultValue = "all") String platform,
                                                      @RequestParam(defaultValue = "all") String genre) {
        return articleService.getArticlePage(EArticleType.NEWS, page, size, platform, genre);
    }

    @GetMapping("/trending-news")
    public List<ArticleGetDto> getTrendingNews(){
        return articleService.getFirstTenNewsByCreatedTime();
    }

    @GetMapping("/reviews")
    public PagingDto<List<MiniArticleGetDto>> getReviews(@RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(defaultValue = "all") String platform,
                                                         @RequestParam(defaultValue = "all") String genre) {
        return articleService.getArticlePage(EArticleType.REVIEW, page, size, platform, genre);
    }

    @PostMapping("/{articleId}/like")
    @Operation(summary = "Create new like")
    @ResponseStatus(HttpStatus.CREATED)
    public void createLike(@PathVariable Long articleId) {
       likeService.createLike(articleId);
    }

    @DeleteMapping("/{articleId}/Like")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable Long articleId) {
        likeService.deleteLike(articleId);
    }

    @GetMapping("/{articleId}/like-num")
    public int getLikeNumForArticle(@PathVariable Long articleId) {
        return likeService.getLikeNumByArticleId(articleId);
    }

    @GetMapping("/reviews/comment-num")
    public PagingDto<List<MiniArticleGetDto>> getCommentNumForArticle(@RequestParam(defaultValue = "1") int page,
                                                                      @RequestParam(defaultValue = "5") int size) {
        return articleService.getPopularReviewArticlesByCommentNum(page,size,EArticleType.REVIEW);
    }


}
