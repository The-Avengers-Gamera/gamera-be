package com.avengers.gamera.service;

import com.avengers.gamera.constant.EArticleType;
import com.avengers.gamera.dto.article.ArticleGetDto;
import com.avengers.gamera.entity.Article;
import com.avengers.gamera.mapper.ArticleMapper;
import com.avengers.gamera.repository.ArticleRepository;
import com.avengers.gamera.util.CurrentUserController;
import com.avengers.gamera.util.MockArticleData;

import com.avengers.gamera.dto.article.ArticlePostDto;
import com.avengers.gamera.entity.Game;
import com.avengers.gamera.entity.Genre;
import com.avengers.gamera.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.time.OffsetDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticleMapper articleMapper;

    @Mock
    private GameService gameService;

    @Mock
    private UserService userService;

    @Mock
    private CurrentUserController currentUserController;


    @InjectMocks
    private ArticleService articleService;

    private final ArticlePostDto articlePostD=ArticlePostDto
            .builder()
            .coverImgUrl("https://assets-prd.ignimgs.com/2023/02/20/legend-of-zelda-tears-of-the-kingdom-1663081213439-1675804568959-1676863480057.jpg?fit=crop&width=282&height=282&dpr=2")
            .gameId(1L)
            .authorId(2L)
            .title("review for last of us")
            .text("review body text for last of us")
            .type(EArticleType.REVIEW).build();

    Genre mockGenre2 = Genre.builder().id(1L).name("ZZ").createdTime(OffsetDateTime.now()).updatedTime(OffsetDateTime.now()).build();

    List<Genre> updatedGenreList = List.of(mockGenre2);
    private final Game mockGame = Game.builder()
            .id(1L)
            .name("Game1")
            .description("Excellent game")
            .isDeleted(false)
            .createdTime(OffsetDateTime.now())
            .updatedTime(OffsetDateTime.now())
            .genreList(updatedGenreList)
            .build();

    private final User mockUser = User.builder()
            .id(2L)
            .name("Bob")
            .email("Bob@gmail.com")
            .createdTime(OffsetDateTime.now())
            .updatedTime(OffsetDateTime.now())
            .build();

    private final Long mockerId = mockUser.getId();

    private final Article mockArticle = Article.builder()
            .coverImgUrl("https://assets-prd.ignimgs.com/2023/02/20/legend-of-zelda-tears-of-the-kingdom-1663081213439-1675804568959-1676863480057.jpg?fit=crop&width=282&height=282&dpr=2")
            .title("review for last of us")
            .type(EArticleType.REVIEW)
            .text("review body text for last of us")
            .author(mockUser)
            .game(mockGame)
            .build();

    private final ArticleGetDto mockArticleGetDto = ArticleGetDto.builder().build();

    @Test
    void shouldSaveNewArticleWhenCreateArticle() {
        when(articleMapper.articlePostDtoToArticle(articlePostD)).thenReturn(mockArticle);
        when(currentUserController.getUserId()).thenReturn(mockerId);
        when(userService.findUser(mockerId)).thenReturn(mockUser);
        when(gameService.findActiveGame(articlePostD.getGameId())).thenReturn(mockGame);
        when(articleMapper.articleToArticleGetDto(any())).thenReturn(mockArticleGetDto);

        ArticleGetDto articleGetDto = articleService.createArticle(articlePostD, EArticleType.NEWS);

        assertEquals(articleGetDto, mockArticleGetDto);
        verify(articleRepository).save(mockArticle);
    }

    ArticleGetDto ExpectUpdatedArticleGetDto = ArticleGetDto.builder().id(MockArticleData.articleId)
            .game(null)
            .user(null)
            .commentList(new ArrayList<>())
            .coverImgUrl("url")
            .title("update title")
            .text("update text")
            .type(EArticleType.REVIEW)
            .createdTime(OffsetDateTime.now())
            .updatedTime(OffsetDateTime.now()).build();

    private ArticleGetDto generateArticleGetDto(Article article) {
        return ArticleGetDto.builder().id(MockArticleData.articleId)
                .game(null)
                .user(null)
                .commentList(new ArrayList<>())
                .coverImgUrl(article.getCoverImgUrl())
                .title(article.getTitle())
                .text(article.getText())
                .type(EArticleType.REVIEW)
                .createdTime(article.getCreatedTime())
                .updatedTime(OffsetDateTime.now()).build();
    }

    @Test
    void updateArticleTest() {
        when(articleRepository.findById(MockArticleData.articleId)).thenReturn(Optional.ofNullable(MockArticleData.mockArticle));
        when(articleRepository.save(any())).thenReturn(MockArticleData.mockArticle);
        when(articleMapper.articleToArticleGetDto(MockArticleData.mockArticle)).thenAnswer(new Answer<ArticleGetDto>() {
            @Override
            public ArticleGetDto answer(InvocationOnMock invocation) throws Throwable {
                Article article = (Article) invocation.getArguments()[0];
                return generateArticleGetDto(article);
            }
        });
        ArticleGetDto updatedArticleGetDto = articleService.updateArticle(MockArticleData.mockArticlePutDto, MockArticleData.articleId);

        assertEquals(ExpectUpdatedArticleGetDto.getText(), updatedArticleGetDto.getText());
        assertEquals(ExpectUpdatedArticleGetDto.getTitle(), updatedArticleGetDto.getTitle());
    }


}