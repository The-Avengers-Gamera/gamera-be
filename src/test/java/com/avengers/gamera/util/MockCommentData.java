package com.avengers.gamera.utils;

import com.avengers.gamera.entity.Comment;

import java.time.OffsetDateTime;

public class MockCommentData {
    public static final Long commentId = 1L;

    public static Comment mockComment = Comment.builder().id(commentId)
            .parentComment(null)
            .text("comment")
            .isDeleted(false)
            .user(MockUserData.mockUser)
            .article(MockArticleData.mockArticle)
            .createdTime(OffsetDateTime.now())
            .updatedTime(OffsetDateTime.now()).build();
}
