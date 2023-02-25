package com.avengers.gamera.service;

import com.avengers.gamera.dto.article.ArticleGetDto;
import com.avengers.gamera.dto.article.ArticlePatchDto;
import com.avengers.gamera.dto.article.ArticlePostDto;
import com.avengers.gamera.entity.Article;
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.avengers.gamera.mapper.ArticleMapper;
import com.avengers.gamera.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    public ArticleGetDto createArticle(ArticlePostDto articlePostDto) {

        Article article = articleMapper.articlePostDtoToArticle(articlePostDto);
        String img = article.getCoverImgUrl();

        //TODO after image upload function implemented, this url should from user
        if (StringUtils.isBlank(img)) {
            article.setCoverImgUrl("https://spicsum.photos/800/400");
        }
        log.info("Saving new article to database");
        return articleMapper.articleToArticleGetDto(articleRepository.save(article));
    }

    public ArticleGetDto updateArticle(Long id, ArticlePatchDto articlePatchDto){
        Article article = articleRepository.findById(id).orElseThrow(() -> (new ResourceNotFoundException("Article")));

        article.setTitle(articlePatchDto.getTitle());
        article.setText(articlePatchDto.getText());
        article.setType(articlePatchDto.getType());

        log.info("Updating article by article id");

        return articleMapper.articleToArticleGetDto(articleRepository.save(article));
    }
}
