package com.avengers.gamera.service;

import com.avengers.gamera.auth.GameraUserDetails;
import com.avengers.gamera.entity.Article;
import com.avengers.gamera.entity.User;
import com.avengers.gamera.exception.ResourceExistException;
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.avengers.gamera.repository.ArticleRepository;
import com.avengers.gamera.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public User getUser() {
        Long userId;
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof GameraUserDetails userDetails) {
            userId = userDetails.getId();
            User user = userRepository.findUserByIdAndIsDeletedFalse(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));
            return user;
        }
        throw new RuntimeException("userId can not be found");
    }

    @Transactional
    public void createLike(Long articleId) {
        User user = this.getUser();
        Article article = articleRepository.findArticleByIdAndIsDeletedFalse(articleId).orElseThrow(() -> new ResourceNotFoundException("article not found"));
        List<Article> likedArticles = user.getLikedArticles();
        if (likedArticles.stream().map(Article::getId).toList().contains(articleId)){
            log.error("user {} already liked article {}",user.getId(),articleId);
            throw new ResourceExistException();
        }
        likedArticles.add(article);
        user.setLikedArticles(likedArticles);
        userRepository.save(user);
        article.getLikeUsers().add(user);
        article.setLikeUsers(article.getLikeUsers());
        article.setLikeNum(article.getLikeUsers().size());
        articleRepository.save(article);
        log.info("Successfully create new like: userId {} articleId {}",user.getId(),articleId);
    }

    public int getLikeNumByArticleId(Long articleId) {
        return articleRepository.findArticleByIdAndIsDeletedFalse(articleId).orElseThrow(()-> new ResourceNotFoundException("article not found")).getLikeUsers().size();
    }

    @Transactional
    public void deleteLike(Long articleId) {
        User user = this.getUser();
        Article article = articleRepository.findArticleByIdAndIsDeletedFalse(articleId).orElseThrow(() -> new ResourceNotFoundException("article not found"));
        List<Article> likedArticles = user.getLikedArticles();
        if (!likedArticles.stream().map(Article::getId).toList().contains(articleId)){
            log.error("user {} and article {} like relation not exists",user.getId(),articleId);
            throw new ResourceExistException();
        }
        likedArticles.remove(article);
        user.setLikedArticles(likedArticles);
        userRepository.save(user);
        article.getLikeUsers().remove(user);
        article.setLikeUsers(article.getLikeUsers());
        articleRepository.save(article);
        log.info("Successfully delete like: userId {} articleId {}",user.getId(),articleId);
    }


}
