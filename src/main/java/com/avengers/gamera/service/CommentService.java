package com.avengers.gamera.service;

import com.avengers.gamera.dto.comment.CommentGetDto;
import com.avengers.gamera.dto.comment.CommentPostDto;
import com.avengers.gamera.dto.comment.CommentPutDto;
import com.avengers.gamera.entity.Article;
import com.avengers.gamera.entity.Comment;
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.avengers.gamera.mapper.CommentMapper;
import com.avengers.gamera.repository.ArticleRepository;
import com.avengers.gamera.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final ArticleRepository articleRepository;

    public CommentGetDto createNewComment(CommentPostDto commentPostDto) {
        Comment comment = commentMapper.commentPostDtoToComment(commentPostDto);
        comment.setUser(userService.findUser(commentPostDto.getAuthorId()));
        if (commentPostDto.getParentId() != null) {
            comment.setParentComment(find(commentPostDto.getParentId()));
        }
        comment.setArticle(findArticleById(commentPostDto.getArticleId()));
        return commentMapper.commentToCommentGetDto(commentRepository.save(comment));
    }

    public Map<String, Object> getCommentByCommentId(Long commentId) {
        Comment comment = commentRepository.findCommentByIdAndIsDeletedFalse(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", commentId));
        Map<String, Object> commentResponse = new HashMap<>();
        commentResponse.put("id", commentId);
        commentResponse.put("text", comment.getText());
        commentResponse.put("createdTime", comment.getCreatedTime());
        commentResponse.put("updatedTime", comment.getUpdatedTime());
        return commentResponse;
    }

    public CommentGetDto updateComment(Long commentId, CommentPutDto commentPutDto) {
        Comment comment = find(commentId);
        comment.setText(commentPutDto.getText());
        return commentMapper.commentToCommentGetDto(commentRepository.save(comment));
    }

    @Modifying
    public void deleteComment(Long commentId) {
        int deleteResponse = commentRepository.deleteCommentById(commentId);
        if (deleteResponse != 1L) {
            throw new ResourceNotFoundException("Comment", commentId);
        }
    }

    public Comment find(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", commentId));
    }

    private Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() -> new ResourceNotFoundException("Article", articleId));
    }

}
