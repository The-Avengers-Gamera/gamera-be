package com.avengers.gamera.repository;

import com.avengers.gamera.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Modifying
    @Query(value = "UPDATE comment SET is_deleted=true WHERE id=?", nativeQuery = true)
    int deleteCommentById(Long id);

    Optional<Comment> findCommentByIdAndIsDeletedFalse(Long id);
}
