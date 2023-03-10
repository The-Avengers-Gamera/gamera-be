package com.avengers.gamera.controller;

import com.avengers.gamera.constant.ArticleType;
import com.avengers.gamera.dto.article.ArticleGetDto;
import com.avengers.gamera.dto.article.ArticlePostDto;
import com.avengers.gamera.dto.article.ArticlePutDto;
import com.avengers.gamera.dto.article.MiniArticleGetDto;
import com.avengers.gamera.service.ArticleService;
import com.avengers.gamera.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("articles")
@RequiredArgsConstructor
@Validated
public class ArticleController {
    private final ArticleService articleService;
    private final LikeService likeService;

    @PostMapping
    @Operation(summary = "Create new article")
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleGetDto createArticle(@RequestBody ArticlePostDto articlePostDto) {
        return articleService.createArticle(articlePostDto);
    }

    @PutMapping("/{articleId}")
    @Operation(summary = "Update article by article id")
    @ResponseStatus(HttpStatus.OK)
    public ArticleGetDto updateArticleById(@RequestBody ArticlePutDto articlePutDto,@PathVariable Long articleId){
        return articleService.updateArticle(articlePutDto,articleId);
    }

    @GetMapping("/pages")
    @ResponseStatus(HttpStatus.OK)
    public Page<MiniArticleGetDto> getMiniArticles(@PageableDefault(size = 10) Pageable pageable){
        return articleService.getMiniArticles(pageable);
    }

    @GetMapping("/types/{articleType}")
    @ResponseStatus(HttpStatus.OK)
    public Page<MiniArticleGetDto> getMiniArticlesByType(@PathVariable ArticleType articleType, @PageableDefault(size = 10) Pageable pageable){
        return articleService.getMiniArticlesByType(articleType,pageable);
    }

    @GetMapping("/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    public ArticleGetDto getArticleById(@PathVariable Long articleId) {
        return articleService.getArticleById(articleId);
    }

    @DeleteMapping("/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteArticleById(@PathVariable Long articleId) {
        return articleService.deleteArticleById(articleId);
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

    @GetMapping("/{articleId}/likeNum")
    public Long getLikeNumForArticle(@PathVariable Long articleId) {
        return likeService.getLikeNumByArticleId(articleId);
    }
}
