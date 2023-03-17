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
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.avengers.gamera.mapper.ArticleMapper;
import com.avengers.gamera.mapper.CommentMapper;
import com.avengers.gamera.mapper.TagMapper;
import com.avengers.gamera.mapper.UserMapper;
import com.avengers.gamera.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    private final TagMapper tagMapper;
    private final UserService userService;
    private final GameService gameService;
    private final TagService tagService;

    public PagingDto<List<MiniArticleGetDto>> getArticlePage(EArticleType articleType, int page, int size, String platform, String genre) {
        Pageable pageable = PageRequest.of(page - 1, size);
        PagingDto<List<MiniArticleGetDto>> data = new PagingDto<>();
        Page<Article> articlePage;
        articlePage = platform.isEmpty() && genre.isEmpty()
                ? articleRepository.findArticlesByTypeAndIsDeletedFalse(articleType, pageable)
                : articleRepository.findArticlesByPlatformAndGenre(platform, genre, pageable);

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

    public ArticleGetDto createArticle(ArticlePostDto articlePostDto) {
        if (articlePostDto.getTagList() != null) {
            List<Tag> updateTagList = handleFrontendTagList(articlePostDto.getTagList());
            articlePostDto.setTagList(updateTagList);
        }
        Article article = articleMapper.articlePostDtoToArticle(articlePostDto);

        String img = article.getCoverImgUrl();
        if (StringUtils.isBlank(img)) {
            article.setCoverImgUrl("https://picsum.photos/800/400");
        }
        article.setAuthor(userService.findUser(articlePostDto.getAuthorId()));
        EArticleType articleType = articlePostDto.getType();

        if (articleType == EArticleType.valueOf("REVIEW")) {
            article.setGame(gameService.findActiveGame(articlePostDto.getGameId()));
        } else if (articleType == EArticleType.valueOf("NEWS")) {
            if (articlePostDto.getGameId() != null) {
                article.setGame(gameService.findActiveGame(articlePostDto.getGameId()));
            }
        }
        log.info("Saving the article with title:  " + article.getTitle() + "  to database");
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
                .map((childComments) -> {
                    CommentSlimDto commentSlimDto = new CommentSlimDto();
                    commentSlimDto.setId(childComments.getId());
                    commentSlimDto.setUpdatedTime(childComments.getUpdatedTime());
                    commentSlimDto.setCreatedTime(childComments.getCreatedTime());
                    commentSlimDto.setText(childComments.getText());
                    commentSlimDto.setUser(userMapper.userToUserSlimGetDto(childComments.getUser()));
                    return commentSlimDto;
                }).toList()));
        List<Tag> tagList = article.getTagList().stream().filter(item -> !item.isDeleted()).toList();
        List<TagSlimDto> tagSlimDtoList = tagList.stream().map(tagMapper::tagToTagSlimDto).toList();
        ArticleGetDto articleGetDto = articleMapper.articleToArticleGetDto(article);
        articleGetDto.setCommentList(allParentComments);
        articleGetDto.setTagList(tagSlimDtoList);
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
