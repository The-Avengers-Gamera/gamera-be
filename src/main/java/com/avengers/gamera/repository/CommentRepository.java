package com.avengers.gamera.repository;

import com.avengers.gamera.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Modifying
    @Query("update Comment c set c.isDeleted = true where c.id =:id and c.isDeleted = false")
    int deleteCommentById(@Param("id") Long id);

    Optional<Comment> findCommentByIdAndIsDeletedFalse(Long id);

    List<Long> findArticleIdByUserIdAndIsDeletedFalse(Long id);

    Integer countByUserIdAndIsDeletedFalse(Long userId);

    @Query(value = "SELECT c.article.id FROM Comment c WHERE c.user.id = :userId AND c.isDeleted = false ORDER BY c.createdTime DESC")
    List<Long> findNewestArticleIdsByUserId(@Param("userId") Long userId, Pageable pageable);

}
