package com.avengers.gamera.service;

import com.avengers.gamera.dto.comment.CommentGetDto;
import com.avengers.gamera.dto.comment.CommentPostDto;
import com.avengers.gamera.dto.comment.CommentPutDto;
import com.avengers.gamera.dto.comment.CommentSlimDto;
import com.avengers.gamera.dto.user.UserSlimGetDto;
import com.avengers.gamera.entity.Article;
import com.avengers.gamera.entity.Comment;
import com.avengers.gamera.entity.User;
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.avengers.gamera.mapper.CommentMapper;
import com.avengers.gamera.repository.ArticleRepository;
import com.avengers.gamera.repository.CommentRepository;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ArticleRepository articleRepository;


    @Mock
    private UserService userService;

    final private User user01 = User.builder().id(1L).name("user01").email("user01@gmail.com").createdTime(OffsetDateTime.now()).updatedTime(OffsetDateTime.now()).password("123456").build();
    final private User user02= User.builder().id(2L).name("user02").email("user02@gmail.com").password("123456").build();
    final private UserSlimGetDto user01SlimGetDto = UserSlimGetDto.builder().id(1L).name("user01").build();

    final private Article article01 = Article.builder().id(1L).build();

    final private Comment mockComment01 = Comment.builder()
            .id(1L)
            .text("this is the first comment")
            .user(user01)
            .build();

    final private CommentSlimDto  mockComment01SlimGetDto = CommentSlimDto.builder()
            .id(1L)
            .text("this is the first comment")
            .user(user01SlimGetDto)
            .build();

    final private CommentPostDto mockComment03PostDto = CommentPostDto.builder()
            .text("new comment")
            .parentId(1L)
            .articleId(1L)
            .authorId(1L)
            .build();
    final private Comment mockComment03= Comment.builder()
            .id(3L)
            .parentComment(mockComment01)
            .text("new comment")
            .isDeleted(false)
            .user(user01)
            .article(article01)
            .build();
    final private CommentGetDto mockComment03GetDto= CommentGetDto.builder()
            .id(3L)
            .text("new comment")
            .user(user01SlimGetDto)
            .parentComment(mockComment01SlimGetDto)
            .build();

    final private CommentPutDto mockComment03PutDto = CommentPutDto.builder()
            .text("change the new comment")
            .build();

    final private CommentGetDto mockUpdateComment03GetDto= CommentGetDto.builder()
            .id(3L)
            .text("change the new comment")
            .user(user01SlimGetDto)
            .parentComment(mockComment01SlimGetDto)
            .build();


    @Test
    @DisplayName("createNewComment should save the comment given a valid CommentPostDto")
    void ShouldSaveNewCommentGivenValidCommentPostDto(){
        when(commentMapper.commentPostDtoToComment(mockComment03PostDto)).thenReturn(mockComment03);
        when(commentRepository.findById(mockComment03PostDto.getParentId())).thenReturn(Optional.of(mockComment01));
        when(userService.findUser(mockComment03PostDto.getAuthorId())).thenReturn(user01);
        when(articleRepository.findById(mockComment03PostDto.getArticleId())).thenReturn(Optional.of(article01));
        when(commentMapper.commentToCommentGetDto(any())).thenReturn(mockComment03GetDto);

        CommentGetDto newComment = commentService.createNewComment(mockComment03PostDto);

        verify(commentRepository).save(mockComment03);
        assertEquals(mockComment03GetDto,newComment);
    }

    @Test
    @DisplayName("getCommentByCommentId should return Map of comment given a valid comment ID")
    void shouldGetCommentGivenValidCommentId(){
        when(commentRepository.findCommentByIdAndIsDeletedFalse(mockComment01.getId())).thenReturn(Optional.of(mockComment01));

        Map<String, Object> commentResponseMap = commentService.getCommentByCommentId(mockComment01.getId());

        assertEquals(mockComment01.getId(),commentResponseMap.get("id"));
        assertEquals(mockComment01.getText(),commentResponseMap.get("text"));
        assertEquals(mockComment01.getCreatedTime(),commentResponseMap.get("createdTime"));
        assertEquals(mockComment01.getUpdatedTime(),commentResponseMap.get("updatedTime"));
    }


    @Test
    @DisplayName("updateComment should update the comment given comment ID and commentPutDto")
    void shouldUpdateCommentGivenCommentPutDto(){
        when(commentRepository.findById(mockComment03.getId())).thenReturn(Optional.of(mockComment03));
        mockComment03.setText(mockComment03PutDto.getText());
        when(commentRepository.save(mockComment03)).thenReturn(mockComment03);
        when(commentMapper.commentToCommentGetDto(mockComment03)).thenReturn(mockUpdateComment03GetDto);

        CommentGetDto commentUpdateGetDto = commentService.updateComment(mockComment03.getId(), mockComment03PutDto);

        verify(commentRepository).save(mockComment03);
        assertEquals(mockUpdateComment03GetDto,commentUpdateGetDto);
    }

    @Test
    @DisplayName("updateComment should update the comment given invalid comment ID and commentPutDto")
    void shouldUpdateCommentGivenInvalidCommentPutDto(){
        when(commentRepository.findById(-1L)).thenThrow(new ResourceNotFoundException());

        assertThrows(ResourceNotFoundException.class, ()->{CommentGetDto commentUpdateGetDto = commentService.updateComment(-1L, mockComment03PutDto);});

    }

    @Test
    @DisplayName("deleteComment should delete the comment given valid comment ID")
    void shouldDeleteCommentGivenCommentId(){
        when(commentRepository.deleteCommentById(1L)).thenReturn(1);

        commentService.deleteComment(1L);

        verify(commentRepository).deleteCommentById(1L);

    }

    @Test
    @DisplayName("deleteComment should delete the comment given invalid comment ID")
    void shouldDeleteCommentGivenInvalidCommentId(){
       when(commentRepository.deleteCommentById(-1L)).thenReturn(0);

        assertThrows(ResourceNotFoundException.class,()->commentService.deleteComment(-1L));

        verify(commentRepository).deleteCommentById(-1L);
    }
}
