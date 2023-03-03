package com.avengers.gamera.service;

import com.avengers.gamera.constant.ArticleType;
import com.avengers.gamera.dto.article.MiniArticleGetDto;
import com.avengers.gamera.entity.Article;
import com.avengers.gamera.mapper.ArticleMapper;
import com.avengers.gamera.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {
    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticleMapper articleMapper;
    @InjectMocks
    private ArticleService articleService;



    String mockPlatform="Xbox";

    List<Article> mockArticlesList = List.of(
            Article.builder().id(1L).title("title1").game(null)
                    .text("text 1")
                    .isDeleted(false)
                    .type(ArticleType.NEWS)
                    .coverImgUrl("url1")
                    .createdTime(OffsetDateTime.now())
                    .updatedTime(OffsetDateTime.now()).build(),
            Article.builder().id(2L).title("title2").game(null)
                    .text("text 2")
                    .isDeleted(false)
                    .type(ArticleType.NEWS)
                    .coverImgUrl("url2")
                    .createdTime(OffsetDateTime.now())
                    .updatedTime(OffsetDateTime.now()).build()
    );
    List<MiniArticleGetDto> mockMiniArticleGetDtoList = List.of(
           MiniArticleGetDto.builder().id(1l).title("title1").game(null)
                   .type(ArticleType.NEWS)
                   .createdTime(OffsetDateTime.now())
                   .updatedTime(OffsetDateTime.now())
                   .coverImgUrl("url1")
                   .build(),
            MiniArticleGetDto.builder().id(2l).title("title2").game(null)
                    .type(ArticleType.NEWS)
                    .createdTime(OffsetDateTime.now())
                    .updatedTime(OffsetDateTime.now())
                    .coverImgUrl("url2")
                    .build()
    );
    Pageable mockPageable= PageRequest.of(0,10);
    Page<MiniArticleGetDto> mockPage=new PageImpl<>(mockMiniArticleGetDtoList,mockPageable,mockMiniArticleGetDtoList.size());

    @Test
    void shouldReturnArticlePageWhenGetArticleByPlatform(){
        //Arrange
        when(articleRepository.findArticleByGamePlatformAndIsDeletedFalse(mockPlatform,mockPageable)).thenReturn(mockArticlesList);
        when(articleMapper.articleToMiniArticleGetDto(any())).thenReturn(mockMiniArticleGetDtoList.get(0),mockMiniArticleGetDtoList.get(1));
        //Act
        Page<MiniArticleGetDto> page=articleService.getMiniArticlesByPlatform(mockPageable,mockPlatform);
        //Assert
        assertEquals(page,mockPage);
    }
    @Test
    void shouldReturnArticlePageWhenGetArticleByPlatformAndType(){
        //Arrange
        when(articleRepository.findArticleByGamePlatformAndTypeAndIsDeletedFalse(mockPlatform,ArticleType.NEWS,mockPageable)).thenReturn(mockArticlesList);
        when(articleMapper.articleToMiniArticleGetDto(any())).thenReturn(mockMiniArticleGetDtoList.get(0),mockMiniArticleGetDtoList.get(1));
        //Act
        Page<MiniArticleGetDto> page=articleService.getMiniArticlesByPlatformAndType(ArticleType.NEWS,mockPageable,mockPlatform);
        //Assert
        assertEquals(page,mockPage);
    }
}
