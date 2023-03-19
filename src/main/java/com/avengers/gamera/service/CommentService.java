package com.avengers.gamera.service;

import com.avengers.gamera.dto.article.ArticlePostDto;
import com.avengers.gamera.dto.comment.CommentGetDto;
import com.avengers.gamera.dto.comment.CommentPostDto;
import com.avengers.gamera.dto.comment.CommentPutDto;
import com.avengers.gamera.entity.Article;
import com.avengers.gamera.entity.Comment;
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.avengers.gamera.mapper.ArticleMapper;
import com.avengers.gamera.mapper.CommentMapper;
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
    private final ArticleMapper articleMapper;

    private final ArticleService articleService;


    public CommentGetDto createNewComment(CommentPostDto commentPostDto) {
        Article article = articleService.findById(commentPostDto.getArticleId());
        article.setCommentNum(article.getCommentList().size()+1);

        Comment comment = commentMapper.commentPostDtoToComment(commentPostDto);
        comment.setUser(userService.findUser(commentPostDto.getAuthorId()));

        if (commentPostDto.getParentId() != null) {
            comment.setParentComment(find(commentPostDto.getParentId()));
        }
        comment.setArticle(article);
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

    public CommentGetDto updateComment(Long commentId, CommentPutDto commentPutDto) {
        Comment comment = find(commentId);
        comment.setText(commentPutDto.getText());
        return commentMapper.commentToCommentGetDto(commentRepository.save(comment));
    }

    public void deleteComment(Long commentId) {
        Article article = find(commentId).getArticle();

        int deleteResponse = commentRepository.deleteCommentById(commentId);
        if (deleteResponse != 1L) {
            throw new ResourceNotFoundException("Comment", commentId);
        }
        article.setCommentNum(article.getCommentList().size()-1);
        articleRepository.save(article);
        // write save article function in article service
        log.info("Successfully delete comment: comment id {};  articleId {}",commentId, article.getId());
    }

    public Comment find(Long commentId) {
        return commentRepository.findCommentByIdAndIsDeletedFalse(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", commentId));
    }

//    private Article findArticleById(Long articleId) {
//        return articleRepository.findArticleByIdAndIsDeletedFalse(articleId).orElseThrow(() -> new ResourceNotFoundException("Article", articleId));
//    }

    public int getCommentNumByArticleId(Long articleId) {
        return articleRepository.findArticleByIdAndIsDeletedFalse(articleId).orElseThrow(()-> new ResourceNotFoundException("article not found")).getCommentNum();
    }


}
