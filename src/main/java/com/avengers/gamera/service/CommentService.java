package com.avengers.gamera.service;

import com.avengers.gamera.dto.comment.CommentGetDto;
import com.avengers.gamera.dto.comment.CommentPostDto;
import com.avengers.gamera.dto.comment.CommentPutDto;
import com.avengers.gamera.dto.comment.CommentSlimDto;
import com.avengers.gamera.entity.Article;
import com.avengers.gamera.entity.Comment;
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.avengers.gamera.mapper.CommentMapper;
import com.avengers.gamera.mapper.UserMapper;
import com.avengers.gamera.repository.ArticleRepository;
import com.avengers.gamera.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final UserMapper userMapper;

    public CommentGetDto createNewComment(CommentPostDto commentPostDto) {
        Comment comment = commentMapper.commentPostDtoToComment(commentPostDto);
        comment.setUser(userService.findUser(commentPostDto.getAuthorId()));
        if (commentPostDto.getParentId() != 0L) {
            comment.setParentComment(find(commentPostDto.getParentId()));
        }
        comment.setArticle(findArticleById(commentPostDto.getArticleId()));
        return commentMapper.commentToCommentGetDto(commentRepository.save(comment));
    }

    public Map<String, Object> getCommentByCommentId(Long commentId) {
        Comment comment = find(commentId);
        Map<String, Object> commentResponse = new HashMap<>();
        commentResponse.put("id", commentId);
        commentResponse.put("text", comment.getText());
        commentResponse.put("createdTime", comment.getCreatedTime());
        commentResponse.put("updatedTime", comment.getUpdatedTime());
        return commentResponse;

    }

    public Map<String, Object> getCommentByArticleId(Long articleId) {
        List<Comment> allResults = commentRepository.findByArticleIdAndIsDeletedFalse(articleId);
        List<CommentGetDto> allParentComments = allResults.stream().filter(comment -> Objects.isNull(comment.getParentComment())).sorted(Comparator.comparingLong(Comment::getId)).map(commentMapper::commentToCommentGetDto).toList();
        List<Comment> allChildComments = allResults.stream().filter(comment -> !Objects.isNull(comment.getParentComment())).toList();
        allParentComments.forEach(parent -> {
            parent.setChildComment(allChildComments.stream().filter(child -> Objects.equals(child.getParentComment().getId(), parent.getId())).map((childComments) -> {
                CommentSlimDto comment = new CommentSlimDto();
                comment.setId(childComments.getId());
                comment.setUpdatedTime(childComments.getUpdatedTime());
                comment.setCreatedTime(childComments.getCreatedTime());
                comment.setText(childComments.getText());
                comment.setUser(userMapper.userToUserSlimGetDto(childComments.getUser()));
                return comment;
            }).toList());
        });
        Map<String, Object> commentResponse = new HashMap<>();
        commentResponse.put("articleId", articleId);
        commentResponse.put("commentList", allParentComments);
        return commentResponse;
    }

    public CommentGetDto updateComment(Long commentId, CommentPutDto commentPutDto) {
        Comment comment = find(commentId);
        comment.setText(commentPutDto.getText());
        return commentMapper.commentToCommentGetDto(commentRepository.save(comment));
    }

    public void deleteComment(Long commentId) {
        Comment comment = find(commentId);
        comment.setIsDeleted(true);
        log.info(" Comment id {} was deleted", commentId);
    }

    public Comment find(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", commentId));
    }

    private Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() -> new ResourceNotFoundException("Article", articleId));
    }

}
