package com.avengers.gamera.service;

import com.avengers.gamera.dto.article.ArticleGetDto;
import com.avengers.gamera.entity.Article;
import com.avengers.gamera.entity.User;
import com.avengers.gamera.exception.ResourceExistException;
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.avengers.gamera.mapper.ArticleMapper;
import com.avengers.gamera.repository.ArticleRepository;
import com.avengers.gamera.repository.UserRepository;
import com.avengers.gamera.util.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleMapper articleMapper;
    private final CurrentUser currentUser;
    private final UserService userService;

    public User getUser() {

        return userService.findUser(currentUser.getUserId());

    }

    public Article getArticle(long articleId) {
        return articleRepository.findArticleByIdAndIsDeletedFalse(articleId).orElseThrow(() -> new ResourceNotFoundException("Article"));

    }

    @Transactional
    public void createLike(Long articleId) {
        User user = this.getUser();
        Article article = this.getArticle(articleId);
        List<Article> likedArticles = user.getLikedArticles();
        if (likedArticles.stream().map(Article::getId).toList().contains(articleId)) {
            log.error("user {} already liked article {}", user.getId(), articleId);
            throw new ResourceExistException();
        }
        likedArticles.add(article);
        user.setLikedArticles(likedArticles);
        userRepository.save(user);
        article.getLikeUsers().add(user);
        article.setLikeUsers(article.getLikeUsers());
        articleRepository.save(article);
        log.info("Successfully create new like: userId {} articleId {}", user.getId(), articleId);
    }

    public List<ArticleGetDto> getLikeByUserId() {
        User user = this.getUser();
        List<Article> likedArticles = user.getLikedArticles();
        return likedArticles.stream().map(articleMapper::articleToArticleGetDto).collect(Collectors.toList());
    }

    public int getLikeNumByArticleId(Long articleId) {
        return this.getArticle(articleId).getLikeUsers().size();
    }

    @Transactional
    public void deleteLike(Long articleId) {
        User user = this.getUser();
        Article article = this.getArticle(articleId);
        List<Article> likedArticles = user.getLikedArticles();
        if (!likedArticles.stream().map(Article::getId).toList().contains(articleId)) {
            log.error("user {} and article {} like relation not exists", user.getId(), articleId);
            throw new ResourceExistException();
        }
        likedArticles.remove(article);
        user.setLikedArticles(likedArticles);
        userRepository.save(user);
        article.getLikeUsers().remove(user);
        article.setLikeUsers(article.getLikeUsers());
        articleRepository.save(article);
        log.info("Successfully delete like: userId {} articleId {}", user.getId(), articleId);
    }
}
