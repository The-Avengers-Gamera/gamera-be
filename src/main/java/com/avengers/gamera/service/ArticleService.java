package com.avengers.gamera.service;

import com.avengers.gamera.constant.ArticleType;
import com.avengers.gamera.dto.article.ArticleGetDto;
import com.avengers.gamera.dto.article.ArticlePostDto;
import com.avengers.gamera.dto.article.MiniArticleGetDto;
import com.avengers.gamera.dto.article.ArticlePutDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
            article.setCoverImgUrl("https://picsum.photos/800/400");
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

    public Page<MiniArticleGetDto> getMiniArticles(Pageable pageable){
        List<MiniArticleGetDto> getMiniArticles = articleRepository.findArticleByIsDeletedFalse(pageable)
                .stream()
                .map(articleMapper::articleToMiniArticleGetDto)
                .collect(Collectors.toList());
        return new PageImpl<>(getMiniArticles, pageable, getMiniArticles.size());
    }

    public Page<MiniArticleGetDto> getMiniArticlesByType(ArticleType articleType, Pageable pageable) {
        List<MiniArticleGetDto> getMiniArticles=articleRepository.findArticlesByTypeAndIsDeletedFalse(articleType, pageable)
                .stream()
                .map(articleMapper::articleToMiniArticleGetDto)
                .collect(Collectors.toList());
        return new PageImpl<>(getMiniArticles, pageable, getMiniArticles.size());
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

    public ArticleGetDto updateArticle (ArticlePutDto articlePutDto, Long articleId){
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ResourceNotFoundException("Article",articleId));

        article.setTitle(articlePutDto.getTitle());
        article.setText(articlePutDto.getText());
        article.setUpdatedTime(OffsetDateTime.now());

        log.info("Updated article with id "+articleId+" in the database.");
        return articleMapper.articleToArticleGetDto(articleRepository.save(article));
    }
    public Page<MiniArticleGetDto> getMiniArticlesByPlatform(Pageable pageable, String platform){
        List<MiniArticleGetDto> miniArticleGetDtoList=articleRepository.findArticleByGamePlatformAndIsDeletedFalse(platform,pageable)
                .stream()
                .map(articleMapper::articleToMiniArticleGetDto)
                .toList();
    return new PageImpl<>(miniArticleGetDtoList,pageable,miniArticleGetDtoList.size());
    }

    public Page<MiniArticleGetDto> getMiniArticlesByPlatformAndType(ArticleType articleType, Pageable pageable, String platform) {
        List<MiniArticleGetDto> miniArticleGetDtoList=articleRepository.findArticleByGamePlatformAndTypeAndIsDeletedFalse(platform,articleType,pageable)
                .stream()
                .map(articleMapper::articleToMiniArticleGetDto)
                .toList();
        return new PageImpl<>(miniArticleGetDtoList,pageable,miniArticleGetDtoList.size());



    }
}
