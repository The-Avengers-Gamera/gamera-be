package com.avengers.gamera.repository;

import com.avengers.gamera.constant.EArticleType;
import com.avengers.gamera.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findArticlesByTypeAndIsDeletedFalse(EArticleType EArticleType, Pageable pageable);

    List<Article> findFirst10ByTypeAndIsDeletedFalseOrderByCreatedTimeAsc(EArticleType eArticleTyp);

    Optional<Article> findArticleByIdAndIsDeletedFalse(Long id);

    @Query("select a from Article a inner join a.author u where u.id=:id and u.isDeleted = false and a.isDeleted = false")
    Page<Article> findArticlesByAuthor(@Param("id") Long authorId, Pageable pageable);

    @Query("select distinct a from Comment c inner join c.article a inner join c.user u where u.id=:id and u.isDeleted = false and c.isDeleted = false")
    Page<Article> findArticlesByCommentedUser(@Param("id") Long userId, Pageable pageable);

    @Query("select distinct a from Article a inner join a.likeUsers l where l.id=:id and l.isDeleted = false and a.isDeleted = false")
    Page<Article> findArticlesByLikedUser(@Param("id") Long userId, Pageable pageable);

    @Query("select distinct a from Article a " +
            "left join a.game g " +
            "left join g.genreList r " +
            "where a.type=:type " +
            "and (g.platform like concat ('%',:platform,'%') or :platform= 'all' ) " +
            "and (r.name=:name or :name= 'all') " +
            "and a.isDeleted=false")
    Page<Article> findArticlesByTypeAndPlatformAndGenreAndIsDeletedFalse(@Param("type") EArticleType articleType,
                                                                         @Param("platform") String platform,
                                                                         @Param("name") String genre,
                                                                         Pageable pageable);

    @Query(value = "select a.title from Article a where a.id=?1 and a.isDeleted=false")
    String findTitleByIdAndIsDeletedFalse(Long id);

    Page<Article> findArticlesByTypeAndIsDeletedFalseOrderByCommentNumDesc(EArticleType articleType, Pageable pageable);

    Page<Article> findAllByOrderByLikeNumDesc(Pageable pageable);
}
