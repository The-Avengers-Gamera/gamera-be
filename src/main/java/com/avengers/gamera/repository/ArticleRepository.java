package com.avengers.gamera.repository;

import com.avengers.gamera.constant.EArticleType;
import com.avengers.gamera.entity.Article;
import com.avengers.gamera.entity.User;
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

    Page<Article> findArticlesByAuthor(User user, Pageable pageable);

    @Query("select distinct a from Comment c inner join c.article a where c.user.id=:id and c.isDeleted = false")
    Page<Article> findArticlesByCommentedUser(@Param("id") Long userId, Pageable pageable);

    @Query("select distinct a from Article a inner join a.likeUsers l where l.id=:id and a.isDeleted = false")
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

    List<Article> findAllByIdIn(List<Long> ids);

    List<Article> findAllByAuthor(User author);

    Integer countByAuthorIdAndIsDeletedFalse(Long userId);
    Page<Article> findAllByOrderByLikeNumDesc(Pageable pageable);

    @Query("SELECT a FROM Article a WHERE a.author.id = :authorId AND a.isDeleted = false ORDER BY a.createdTime DESC")
    List<Article> findTopNewestArticlesByAuthorId(@Param("authorId") Long authorId, Pageable pageable);
}
