package com.avengers.gamera.contoller;

import com.avengers.gamera.constant.EArticleType;
import com.avengers.gamera.dto.comment.CommentPostDto;
import com.avengers.gamera.entity.Article;
import com.avengers.gamera.entity.Comment;
import com.avengers.gamera.entity.User;
import com.avengers.gamera.repository.ArticleRepository;
import com.avengers.gamera.repository.CommentRepository;
import com.avengers.gamera.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

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
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    private Comment mockComment01;
    private Comment mockComment;
    private CommentPostDto mockComment01PostDto;
    private CommentPostDto mockComment03PostDto;


    @BeforeEach
    void setup() {
        commentRepository.deleteAll();
        articleRepository.deleteAll();
        userRepository.deleteAll();
        articleRepository.flush();
        commentRepository.flush();
        userRepository.flush();

        User user01 = User.builder().name("user0132").email("user01424@gmail.com").isDeleted(false).password("12FDSAGDggs2;3456").build();
        userRepository.save(user01);

        Article article01 = Article.builder().author(user01).type(EArticleType.NEWS).build();
        articleRepository.save(article01);

        mockComment01PostDto = CommentPostDto.builder()
                .text("the first comment has been changed")
                .build();

        mockComment01 = Comment.builder()
                .article(article01)
                .text("this is the first comment")
                .user(user01)
                .build();
        commentRepository.save(mockComment01);

        mockComment03PostDto = CommentPostDto.builder()
                .text("new comment")
                .parentId(mockComment01.getId())
                .articleId(article01.getId())
                .authorId(article01.getId())
                .build();
    }

    @Test
    @DisplayName("createComment should create a new comment")
    void shouldCreateNewComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockComment03PostDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text").value("new comment"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("should return not found given Invalid commentPostDto")
    void shouldReturnNotFoundGivenInvalidCommentPostDto() throws Exception {
        CommentPostDto errorCommentPostDto = CommentPostDto.builder()
                .text("error comment")
                .articleId(-99L)
                .authorId(-99L)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(errorCommentPostDto)))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("should return a comment given valid comment id")
    void shouldReturnCommentGivenValidCommentId() throws Exception{
        long commentId = mockComment01.getId();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/comments/" + commentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("this is the first comment"));
    }

    @Test
    @DisplayName("should Return Not Found given invalid comment id")
    void shouldReturnNotFoundGivenInvalidCommentId() throws Exception{
        long commentId = -99999L;
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/comments/" + commentId))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("should Return Ok and CommentGetDto Given CommentId And CommentPutDto")
    void shouldReturnOkAndCommentGetDtoGivenCommentIdAndCommentPutDto() throws Exception{
        Long commentId = mockComment01.getId();
        mockMvc.perform(MockMvcRequestBuilders
                .put("/comments/" + commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockComment01PostDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("the first comment has been changed"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("should Return Ok and set the attribute is deleted of the comment to true ")
    void shouldReturnOkAndSetCommentToBeDeleted() throws Exception{
        Long commentId = mockComment01.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete("/comments/" + commentId))
                .andExpect(status().isNoContent()).andDo(MockMvcResultHandlers.print());
    }

}
