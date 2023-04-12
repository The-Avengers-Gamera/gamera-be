package com.avengers.gamera.controller;

import com.avengers.gamera.dto.article.EsArticleGetDto;
import com.avengers.gamera.service.ElasticSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final ElasticSearchService elasticSearchService;

    @GetMapping
    public List<EsArticleGetDto> search(@RequestParam(required = false) String keyword) {
        return elasticSearchService.searchArticle(keyword);
    }
}
