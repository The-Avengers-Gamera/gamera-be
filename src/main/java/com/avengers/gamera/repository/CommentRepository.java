package com.avengers.gamera.repository;

import com.avengers.gamera.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Transactional
    @Modifying
    @Query("update Comment c set c.isDeleted = true where c.id = ?1 and c.isDeleted = false")
    int deleteCommentById(Long id);

    Optional<Comment> findCommentByIdAndIsDeletedFalse(Long id);
}
