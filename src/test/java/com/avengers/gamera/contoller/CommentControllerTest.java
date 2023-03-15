package com.avengers.gamera.contoller;

import com.avengers.gamera.constant.EArticleType;
import com.avengers.gamera.controller.CommentController;
import com.avengers.gamera.dto.comment.CommentGetDto;
import com.avengers.gamera.dto.comment.CommentPostDto;
import com.avengers.gamera.dto.comment.CommentPutDto;
import com.avengers.gamera.dto.comment.CommentSlimDto;
import com.avengers.gamera.dto.genre.GenrePostDto;
import com.avengers.gamera.dto.genre.GenreUpdateDto;
import com.avengers.gamera.dto.user.UserPostDto;
import com.avengers.gamera.dto.user.UserSlimGetDto;
import com.avengers.gamera.entity.Article;
import com.avengers.gamera.entity.Comment;
import com.avengers.gamera.entity.User;
import com.avengers.gamera.mapper.CommentMapper;
import com.avengers.gamera.repository.ArticleRepository;
import com.avengers.gamera.repository.CommentRepository;
import com.avengers.gamera.repository.UserRepository;
import com.avengers.gamera.service.CommentService;
import com.avengers.gamera.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CommentControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CommentService commentService;

//    @Mock
//    private CommentMapper commentMapper;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;


    @Autowired
    private UserService userService;

    private User user01;


    private UserSlimGetDto user01SlimGetDto;

    private Article article01;

    final private Comment mockComment01 = Comment.builder()
            .id(1L)
            .text("this is the first comment")
            .user(user01)
            .build();

    final private CommentSlimDto mockComment01SlimGetDto = CommentSlimDto.builder()
            .id(1L)
            .text("this is the first comment")
            .user(user01SlimGetDto)
            .build();

    private CommentPostDto mockComment03PostDto;
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

    @BeforeEach
    void cleanup() {
        commentRepository.deleteAll();
        commentRepository.flush();

    }

    @Test
    @DisplayName("should create a new comment")
    void shouldCreateNewComment() throws Exception {
//        when(commentMapper.commentPostDtoToComment(mockComment03PostDto)).thenReturn(mockComment03);
//        when(commentRepository.findById(mockComment03PostDto.getParentId())).thenReturn(Optional.of(mockComment01));
//        when(articleRepository.findById(1L)).thenReturn(Optional.of(article01));
//        when(userService.findUser(1L)).thenReturn(user01);

//        when(commentMapper.commentToCommentGetDto(mockComment03)).thenReturn(mockComment03GetDto);
//
//        when(commentService.createNewComment(mockComment03PostDto)).thenReturn(mockComment03GetDto);
        user01 = User.builder().id(12432L).name("user0132").email("user01424@gmail.com").isDeleted(false).password("12FDSAGDggs2;3456").build();
//        UserPostDto user01PostDto = UserPostDto.builder().build();

        userRepository.save(user01);

//        article01 = Article.builder().id(1L).author(user01).type(EArticleType.NEWS).build();
//        articleRepository.save(article01);

//        mockComment03PostDto = CommentPostDto.builder()
//                .text("new comment")
//                .parentId(null)
//                .articleId(1L)
//                .authorId(1L)
//                .build();
//
//        user01SlimGetDto = UserSlimGetDto.builder().id(1L).name("user01").build();
//        mockMvc.perform(MockMvcRequestBuilders.post("/comments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(mockComment03PostDto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.text").value("new comment"))
//                .andDo(MockMvcResultHandlers.print());
    }
}
