package com.avengers.gamera.service;

import com.avengers.gamera.constant.EArticleSort;
import com.avengers.gamera.constant.EArticleType;
import com.avengers.gamera.dto.PagingDto;
import com.avengers.gamera.dto.article.ArticleGetDto;
import com.avengers.gamera.dto.article.MiniArticleGetDto;
import com.avengers.gamera.dto.comment.CommentGetDto;
import com.avengers.gamera.dto.tag.TagSlimDto;
import com.avengers.gamera.dto.user.UserGetDto;
import com.avengers.gamera.dto.user.UserSlimGetDto;
import com.avengers.gamera.entity.*;
import com.avengers.gamera.mapper.ArticleMapper;
import com.avengers.gamera.mapper.CommentMapper;
import com.avengers.gamera.mapper.TagMapper;
import com.avengers.gamera.mapper.UserMapper;
import com.avengers.gamera.repository.ArticleRepository;
import com.avengers.gamera.util.MockArticleData;

import com.avengers.gamera.dto.article.ArticlePostDto;
import com.avengers.gamera.util.MockCommentData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import static com.avengers.gamera.util.SortHelper.getSortOrder;
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
    private CommentMapper commentMapper;

    @Mock(lenient=true)
    private TagMapper tagMapper;

    @Mock
    private LikeService likeService;

    @InjectMocks
    private ArticleService articleService;

    private final ArticlePostDto articlePostDto = ArticlePostDto.builder()
            .coverImgUrl("https://assets-prd.ignimgs.com/2023/02/20/legend-of-zelda-tears-of-the-kingdom-1663081213439-1675804568959-1676863480057.jpg?fit=crop&width=282&height=282&dpr=2")
            .gameId(1L)
            .authorId(2L)
            .title("review for last of us")
            .text("review body text for last of us")
            .type(EArticleType.REVIEW)
            .build();

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
        when(articleMapper.articlePostDtoToArticle(articlePostDto)).thenReturn(mockArticle);
        when(userService.findUser(articlePostDto.getAuthorId())).thenReturn(mockUser);
        when(gameService.findActiveGame(articlePostDto.getGameId())).thenReturn(mockGame);
        when(articleMapper.articleToArticleGetDto(any())).thenReturn(mockArticleGetDto);

        ArticleGetDto articleGetDto =  articleService.createArticle(articlePostDto, EArticleType.NEWS, mockUser.getId());

        assertEquals(articleGetDto, mockArticleGetDto);
        verify(articleRepository).save(mockArticle);
    }

    ArticleGetDto ExpectUpdatedArticleGetDto = ArticleGetDto.builder().id(MockArticleData.articleId)
            .game(null)
            .author(null)
            .commentList(new ArrayList<>())
            .coverImgUrl("url")
            .title("update title")
            .text("update text")
            .type(EArticleType.REVIEW)
            .createdTime(OffsetDateTime.now())
            .updatedTime(OffsetDateTime.now()).build();

    private ArticleGetDto generateArticleGetDto(Article article){
        return  ArticleGetDto.builder().id(MockArticleData.articleId)
                .game(null)
                .author(null)
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
                return generateArticleGetDto (article);
            }
        });
        ArticleGetDto updatedArticleGetDto = articleService.updateArticle(MockArticleData.mockArticlePutDto, MockArticleData.articleId);

        assertEquals(ExpectUpdatedArticleGetDto.getText(), updatedArticleGetDto.getText());
        assertEquals(ExpectUpdatedArticleGetDto.getTitle(), updatedArticleGetDto.getTitle());
    }

    private Page<Article> generateMockArticlePage(Article article) {
        List<Article> articleList = new ArrayList<>();
        articleList.add(article);
        return (Page) new PageImpl(articleList);
    }

    private UserSlimGetDto generateUserGetDto(User user){
        return UserSlimGetDto.builder().id(user.getId())
                .name(user.getName()).build();
    }

    private MiniArticleGetDto generateMiniArticleGetDto(Article article){
        return  MiniArticleGetDto.builder().id(MockArticleData.articleId)
                .game(null)
                .author(generateUserGetDto(article.getAuthor()))
                .commentNum(article.getCommentNum())
                .coverImgUrl(article.getCoverImgUrl())
                .title(article.getTitle())
                .type(EArticleType.REVIEW)
                .createdTime(article.getCreatedTime())
                .updatedTime(article.getUpdatedTime()).build();
    }

    MiniArticleGetDto ExpectMiniArticleGetDto = MiniArticleGetDto.builder().id(MockArticleData.articleId)
            .game(null)
            .author(generateUserGetDto(MockArticleData.mockArticle.getAuthor()))
            .commentNum(1)
            .coverImgUrl("url")
            .title("update title")
            .type(EArticleType.REVIEW)
            .createdTime(OffsetDateTime.now())
            .updatedTime(OffsetDateTime.now()).build();

    @Test
    void getArticlePageTest() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdTime"));
        when(articleRepository.findArticlesByTypeAndIsDeletedFalse(MockArticleData.mockArticle.getType(), pageable)).thenReturn(generateMockArticlePage(MockArticleData.mockArticle));
        when(articleMapper.articleToMiniArticleGetDto(MockArticleData.mockArticle)).thenAnswer(new Answer<MiniArticleGetDto>() {
            @Override
            public MiniArticleGetDto answer(InvocationOnMock invocation) throws Throwable {
                Article article = (Article) invocation.getArguments()[0];
                return generateMiniArticleGetDto(article);
            }
        });
        String order = "desc";
        PagingDto<List<MiniArticleGetDto>> getArticlePageDto = articleService.getArticlePage(EArticleType.REVIEW, 1, 10, "all", "all", EArticleSort.CREATED_TIME, getSortOrder(order));
        assertEquals(ExpectMiniArticleGetDto.getCommentNum(), getArticlePageDto.getData().get(0).getCommentNum());
        assertEquals(ExpectMiniArticleGetDto.getTitle(), getArticlePageDto.getData().get(0).getTitle());
    }

    CommentGetDto generateCommentGetDto(Comment comment) {
        CommentGetDto commentGetDto = new CommentGetDto(comment.getId(), comment.getText(), null, null, null, comment.getCreatedTime(), comment.getUpdatedTime());
        return commentGetDto;
    }

    TagSlimDto generateTagSlimDto() {
        TagSlimDto tagSlimDto = new TagSlimDto(1L, "horror");
        return tagSlimDto;
    }

    @Test
    void getArticleByIdTest() {
        when(articleRepository.findArticleByIdAndIsDeletedFalse(any())).thenReturn(Optional.ofNullable(MockArticleData.mockArticle));
        when(commentMapper.commentToCommentGetDto(any())).thenReturn(generateCommentGetDto(MockCommentData.mockComment));
        when(tagMapper.tagToTagSlimDto(any())).thenReturn(generateTagSlimDto());
        when(articleMapper.articleToArticleGetDto(MockArticleData.mockArticle)).thenAnswer(new Answer<ArticleGetDto>() {
            @Override
            public ArticleGetDto answer(InvocationOnMock invocation) throws Throwable {
                Article article = (Article) invocation.getArguments()[0];
                return generateArticleGetDto (article);
            }
        });
        ArticleGetDto articleGetDto = articleService.getArticleById(MockArticleData.articleId);
        assertEquals("update title", articleGetDto.getTitle());
        assertEquals("update text", articleGetDto.getText());
    }

    @Test
    void deleteArticleByIdTest() {
        when(articleRepository.findArticleByIdAndIsDeletedFalse(MockArticleData.articleId)).thenReturn(Optional.ofNullable(MockArticleData.mockArticle));
        articleService.deleteArticleById(MockArticleData.articleId);
        verify(articleRepository).save(MockArticleData.mockArticle);
    }
}