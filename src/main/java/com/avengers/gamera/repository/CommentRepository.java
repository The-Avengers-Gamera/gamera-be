package com.avengers.gamera.repository;

import com.avengers.gamera.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleIdAndIsDeletedFalse(Long articleId);
}
