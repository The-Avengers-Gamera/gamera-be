package com.avengers.gamera.service;

import com.avengers.gamera.constant.ArticleType;
import com.avengers.gamera.dto.article.ArticleGetDto;
import com.avengers.gamera.dto.article.ArticlePatchDto;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final ArticleMapper articleMapper;

    public ArticleGetDto createArticle(ArticlePostDto articlePostDto) {

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
                new ResourceNotFoundException("Related Author(user)")
        );
        article.setUser(user);
        log.info("Saving new article: "+article.toString()+" ======>>to database");
        return articleMapper.articleToArticleGetDto(articleRepository.save(article));
    }

    public List<MiniArticleGetDto> getMiniArticles(int pageNumber, int pageSize){
        List<Article> articles = articleRepository.findArticleByIsDeletedFalse((Pageable) PageRequest.of(pageNumber,pageSize));
        List<MiniArticleGetDto> listMiniArticles = new ArrayList<>();
        for (Article article : articles) {
            MiniArticleGetDto miniArticle = articleMapper.articleToMiniArticleGetDto(article);
            log.info("miniArticle got by page ==> " + article.toString());
            listMiniArticles.add(miniArticle);
        }
        return listMiniArticles;

    }

    public List<MiniArticleGetDto> getMiniArticlesByType(ArticleType articleType, int pageNumber, int pageSize) {
        List<Article> articles = articleRepository.findArticlesByTypeAndIsDeletedFalse(articleType, (Pageable) PageRequest.of(pageNumber, pageSize));
        List<MiniArticleGetDto> listMiniArticles = new ArrayList<>();
        for (Article article : articles) {
            MiniArticleGetDto miniArticle = articleMapper.articleToMiniArticleGetDto(article);
            log.info("miniArticle got by type ==> " + article.toString());
            listMiniArticles.add(miniArticle);
        }
        return listMiniArticles;
    }

    public ArticleGetDto getArticleById(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() ->
                new ResourceNotFoundException("Related Article with the ID(" + articleId + ")")
        );
        return articleMapper.articleToArticleGetDto(article);
    }

    public void deleteArticleById(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() ->
                new ResourceNotFoundException("Related Article with the ID(" + articleId + ")")
        );
       article.setDeleted(true);
    }

    public ArticleGetDto updateArticle( ArticlePatchDto articlePatchDto){
        Long ArticleId = articlePatchDto.getId();
        Article article = articleRepository.findById(ArticleId).orElseThrow(() -> (new ResourceNotFoundException("Article with id("+ ArticleId +")")));

        article.setTitle(articlePatchDto.getTitle());
        article.setText(articlePatchDto.getText());
        article.setType(articlePatchDto.getType());

        log.info("Updating article ==>>" + article.toString());

        return articleMapper.articleToArticleGetDto(articleRepository.save(article));
    }
}
