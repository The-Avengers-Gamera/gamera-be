package com.avengers.gamera.controller;

import com.avengers.gamera.constant.ArticleType;
import com.avengers.gamera.dto.article.ArticleGetDto;
import com.avengers.gamera.dto.article.ArticlePatchDto;
import com.avengers.gamera.dto.article.ArticlePostDto;
import com.avengers.gamera.dto.article.MiniArticleGetDto;
import com.avengers.gamera.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;


import java.util.List;

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

    @PutMapping("/update")
    @Operation(summary = "Update article by article id")
    @ResponseStatus(HttpStatus.OK)
    public ArticleGetDto updateArticle(@RequestBody ArticlePatchDto articlePatchDto){
        return articleService.updateArticle(articlePatchDto);
    }
    @GetMapping("/pages/{pageNumber}/{pageSize}")
    public List<MiniArticleGetDto> getMiniArticles(@PathVariable Integer pageNumber, @PathVariable Integer pageSize){
        return articleService.getMiniArticles(pageNumber,pageSize);
    }

    @GetMapping("/types/{articleType}/{pageNumber}/{pageSize}")
    public List<MiniArticleGetDto> getMiniArticlesByType(@PathVariable ArticleType articleType, @PathVariable Integer pageNumber, @PathVariable Integer pageSize){
        return articleService.getMiniArticlesByType(articleType,pageNumber,pageSize);
    }

    @GetMapping("/{articleId}")
    public ArticleGetDto createArticle(@PathVariable Long articleId) {
        return articleService.getArticleById(articleId);
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<String> deleteArticleById(@PathVariable Long articleId) {
        articleService.deleteArticleById(articleId);
        return ResponseEntity.ok("the article with ID("+ articleId +") has been deleted");
    }
}
