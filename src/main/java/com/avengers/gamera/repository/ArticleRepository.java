package com.avengers.gamera.repository;

import com.avengers.gamera.constant.EArticleType;
import com.avengers.gamera.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findArticlesByTypeAndIsDeletedFalse(EArticleType EArticleType, Pageable pageable);

    Optional<Article> findArticleByIdAndIsDeletedFalse(Long id);

    @Transactional
    @Query ("select a from Article a join a.game g join g.genreList r where (g.platform like concat ('%',:platform,'%') or :platform = '' ) and (r.name=:name or :name = '')")
    Page<Article> findArticlesByPlatformAndGenre(@Param("platform") String platform, @Param("name") String genre, Pageable pageable);

}
