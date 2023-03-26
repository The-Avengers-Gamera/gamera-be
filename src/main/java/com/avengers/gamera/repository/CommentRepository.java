package com.avengers.gamera.repository;

import com.avengers.gamera.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Modifying
    @Query("update Comment c set c.isDeleted = true where c.id =:id and c.isDeleted = false")
    int deleteCommentById(@Param("id") Long id);

    Optional<Comment> findCommentByIdAndIsDeletedFalse(Long id);
}
