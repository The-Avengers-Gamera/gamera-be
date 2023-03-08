package com.avengers.gamera.repository;

import com.avengers.gamera.constant.EArticleType;
import com.avengers.gamera.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findArticlesByTypeAndIsDeletedFalse(EArticleType EArticleType, Pageable pageable);

    Optional<Article> findArticleByIdAndIsDeletedFalse(Long id);
}
