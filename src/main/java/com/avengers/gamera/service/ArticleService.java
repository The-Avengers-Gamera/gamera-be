package com.avengers.gamera.service;

import com.avengers.gamera.constant.ArticleType;
import com.avengers.gamera.dto.article.ArticleGetDto;
import com.avengers.gamera.dto.article.ArticlePostDto;
import com.avengers.gamera.dto.article.MiniArticleGetDto;
import com.avengers.gamera.entity.Article;
import com.avengers.gamera.entity.Game;
import com.avengers.gamera.entity.User;
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.avengers.gamera.mapper.ArticleMapper;
import com.avengers.gamera.repository.ArticleRepository;
import com.avengers.gamera.repository.GameRepository;
import com.avengers.gamera.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final ArticleMapper articleMapper;



    public ArticleGetDto createArticle(ArticlePostDto articlePostDto) {
        Article article = articlePostDtoToArticle(articlePostDto);
        log.info("Saving the article with title:  "+article.getTitle()+"  to database");
        return articleMapper.articleToArticleGetDto(articleRepository.save(article));
    }

    public Article articlePostDtoToArticle(ArticlePostDto articlePostDto){
        Article article = articleMapper.articlePostDtoToArticle(articlePostDto);
        String img = article.getCoverImgUrl();
        if (StringUtils.isBlank(img)) {
            article.setCoverImgUrl("https://spicsum.photos/800/400");
        }
        Game game = gameRepository.findById(articlePostDto.getGameId()).orElseThrow(()->
                new ResourceNotFoundException("Related game with ID("+ articlePostDto.getGameId() +")" )
        );
        article.setGame(game);
        User user = userRepository.findById(articlePostDto.getAuthorId()).orElseThrow(() ->
                new ResourceNotFoundException("Related Author(userId: "+ articlePostDto.getAuthorId() +")")
        );
        article.setUser(user);
        return article;
    }

    public List<MiniArticleGetDto> getMiniArticles(Pageable pageable){
        return articleRepository.findArticleByIsDeletedFalse(pageable)
                .stream()
                .map(articleMapper::articleToMiniArticleGetDto)
                .collect(Collectors.toList());
    }

    public List<MiniArticleGetDto> getMiniArticlesByType(ArticleType articleType, Pageable pageable) {
        return articleRepository.findArticlesByTypeAndIsDeletedFalse(articleType, pageable)
                .stream()
                .map(articleMapper::articleToMiniArticleGetDto)
                .collect(Collectors.toList());
    }


    public ArticleGetDto getArticleById(Long articleId) {
        Article article = articleRepository.findArticleByIdAndIsDeletedFalse(articleId).orElseThrow(() ->
                new ResourceNotFoundException("Related Article with the ID(" + articleId + ")")
        );
        return articleMapper.articleToArticleGetDto(article);
    }

    public String deleteArticleById(Long articleId) {
        Article article = articleRepository.findArticleByIdAndIsDeletedFalse(articleId).orElseThrow(() ->
                new ResourceNotFoundException("Related Article with the ID(" + articleId + ")")
        );
        log.info("Article with ID("+ articleId +") title("+ article.getTitle() +") is being deleted");
        article.setDeleted(true);
        articleRepository.save(article);
       return "The article with ID("+ articleId +") has been deleted";
    }

    public ArticleGetDto updateArticle( Long articleId,ArticlePostDto articlePostDto){

        Article findArticle = articleRepository.findArticleByIdAndIsDeletedFalse(articleId).orElseThrow(() -> (new ResourceNotFoundException("Article with id("+ articleId +")")));
        Article article = articlePostDtoToArticle(articlePostDto);
        article.setId(articleId);
        article.setCreatedTime(findArticle.getCreatedTime());
        article.setUpdatedTime(OffsetDateTime.now());
        log.info("Updating article ==>> ID: " + articleId +"title: "+ articlePostDto.getTitle()+"update time: "+OffsetDateTime.now());
        return articleMapper.articleToArticleGetDto(articleRepository.save(article));
    }

}
