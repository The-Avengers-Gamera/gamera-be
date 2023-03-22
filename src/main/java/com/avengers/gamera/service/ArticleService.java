package com.avengers.gamera.service;

import com.avengers.gamera.constant.EArticleType;
import com.avengers.gamera.dto.PagingDto;
import com.avengers.gamera.dto.article.ArticleGetDto;
import com.avengers.gamera.dto.article.ArticlePostDto;
import com.avengers.gamera.dto.article.MiniArticleGetDto;
import com.avengers.gamera.dto.article.ArticlePutDto;
import com.avengers.gamera.dto.comment.CommentGetDto;
import com.avengers.gamera.dto.comment.CommentSlimDto;
import com.avengers.gamera.dto.tag.TagSlimDto;
import com.avengers.gamera.entity.*;
import com.avengers.gamera.exception.ArgumentNotValidException;
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.avengers.gamera.mapper.ArticleMapper;
import com.avengers.gamera.mapper.CommentMapper;
import com.avengers.gamera.mapper.TagMapper;
import com.avengers.gamera.mapper.UserMapper;
import com.avengers.gamera.repository.ArticleRepository;
import com.avengers.gamera.util.CurrentUserController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final CommentMapper commentMapper;
    private final UserMapper userMapper;
    private final LikeService likeService;
    private final TagMapper tagMapper;
    private final UserService userService;
    private final GameService gameService;
    private final TagService tagService;

    private final CurrentUserController currentUserController;

    public PagingDto<List<MiniArticleGetDto>> getArticlePage(EArticleType articleType, int page, int size, String platform, String genre) {
        Pageable pageable = PageRequest.of(page - 1, size);
        PagingDto<List<MiniArticleGetDto>> data = new PagingDto<>();
        Page<Article> articlePage;
        articlePage = platform.equals("all") & genre.equals("all")
                ? articleRepository.findArticlesByTypeAndIsDeletedFalse(articleType, pageable)
                : articleRepository.findArticlesByTypeAndPlatformAndGenreAndIsDeletedFalse(articleType, platform, genre, pageable);

        List<MiniArticleGetDto> miniArticleGetDtoList = articlePage.getContent()
                .stream()
                .map(articleMapper::articleToMiniArticleGetDto)
                .toList();
        data.setData(miniArticleGetDtoList);
        data.setCurrentPage(articlePage.getNumber() + 1);
        data.setTotalPages(articlePage.getTotalPages());
        data.setTotalItems(articlePage.getTotalElements());

        return data;
    }

    public ArticleGetDto createArticle(ArticlePostDto articlePostDto, EArticleType eArticleType) {

        if (eArticleType == EArticleType.REVIEW && articlePostDto.getGameId() == null) {
            throw new ArgumentNotValidException();
        }

        return handleCreateArticle(articlePostDto, eArticleType);
    }

    @Transactional
    public ArticleGetDto handleCreateArticle(ArticlePostDto articlePostDto, EArticleType eArticleType) {

        Article article = articleMapper.articlePostDtoToArticle(articlePostDto);

        if (articlePostDto.getGameId() != null) {
            article.setGame(gameService.findActiveGame(articlePostDto.getGameId()));
        }

        if (articlePostDto.getTagList() != null) {
            List<Tag> updateTagList = handleFrontendTagList(articlePostDto.getTagList());
            articlePostDto.setTagList(updateTagList);
        }

        article.setType(eArticleType);
        article.setAuthor(userService.findUser(currentUserController.getUserId()));

        log.info("Saving the" + article.getType() + "with title:  " + article.getTitle() + "  to database");

        return articleMapper.articleToArticleGetDto(articleRepository.save(article));
    }

    public List<Tag> handleFrontendTagList(List<Tag> tagList) {
        Map<Boolean, List<Tag>> checkTags = tagList.stream().collect(Collectors.partitioningBy(item -> item.getId() == null));
        List<Tag> newTagFromUser = checkTags.get(true);
        List<Tag> existTagFromUser = checkTags.get(false);
        List<Tag> existTag = tagService.getAllTag(existTagFromUser);
        List<Tag> updatedTagList = new ArrayList<>(existTag);

        if (newTagFromUser.size() > 0) {
            List<Tag> createdTag = tagService.createMultipleTag(newTagFromUser);
            updatedTagList.addAll(createdTag);
        }

        return updatedTagList;
    }

    public ArticleGetDto getArticleById(Long articleId) {
        Article article = articleRepository.findArticleByIdAndIsDeletedFalse(articleId).orElseThrow(() ->
                new ResourceNotFoundException("Related Article with the ID(" + articleId + ")")
        );

        List<Comment> allResults = article.getCommentList().stream().filter(item -> !item.getIsDeleted()).toList();
        List<CommentGetDto> allParentComments = allResults.stream()
                .filter(comment -> Objects.isNull(comment.getParentComment()))
                .sorted(Comparator.comparingLong(Comment::getId))
                .map(commentMapper::commentToCommentGetDto).toList();
        List<Comment> allChildComments = allResults.stream()
                .filter(comment -> !Objects.isNull(comment.getParentComment())).toList();

        allParentComments.forEach(parent -> parent.setChildComment(allChildComments.stream()
                .filter(child -> Objects.equals(child.getParentComment().getId(), parent.getId()))
                .map((childComments) -> CommentSlimDto.builder()
                        .id(childComments.getId())
                        .updatedTime(childComments.getUpdatedTime())
                        .createdTime(childComments.getCreatedTime())
                        .text(childComments.getText())
                        .user(userMapper.userToUserSlimGetDto(childComments.getUser()))
                        .build()).toList()));

        List<Tag> tagList = article.getTagList().stream().filter(item -> !item.isDeleted()).toList();
        List<TagSlimDto> tagSlimDtoList = tagList.stream().map(tagMapper::tagToTagSlimDto).toList();

        ArticleGetDto articleGetDto = articleMapper.articleToArticleGetDto(article);
        articleGetDto.setCommentList(allParentComments);
        articleGetDto.setTagList(tagSlimDtoList);
        //For LikeNum
        articleGetDto.setLikeNum(likeService.getLikeNumByArticleId(articleId));
        return articleGetDto;
    }

    public String deleteArticleById(Long articleId) {
        Article article = articleRepository.findArticleByIdAndIsDeletedFalse(articleId).orElseThrow(() ->
                new ResourceNotFoundException("Related Article with the ID(" + articleId + ")")
        );
        log.info("Article with ID(" + articleId + ") title(" + article.getTitle() + ") is being deleted");
        article.setDeleted(true);
        articleRepository.save(article);
        return "The article with ID(" + articleId + ") has been deleted";
    }

    public ArticleGetDto updateArticle(ArticlePutDto articlePutDto, Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ResourceNotFoundException("Article", articleId));

        article.setTitle(articlePutDto.getTitle());
        article.setText(articlePutDto.getText());
        article.setUpdatedTime(OffsetDateTime.now());

        log.info("Updated article with id " + articleId + " in the database.");
        return articleMapper.articleToArticleGetDto(articleRepository.save(article));
    }
}
