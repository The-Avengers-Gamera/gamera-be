package com.avengers.gamera.service;

import com.avengers.gamera.constant.EArticleSort;
import com.avengers.gamera.constant.EArticleType;
import com.avengers.gamera.constant.EUserArticleType;
import com.avengers.gamera.dto.PagingDto;
import com.avengers.gamera.dto.article.ArticleGetDto;
import com.avengers.gamera.dto.article.ArticlePostDto;
import com.avengers.gamera.dto.article.ArticlePutDto;
import com.avengers.gamera.dto.article.MiniArticleGetDto;
import com.avengers.gamera.dto.comment.CommentGetDto;
import com.avengers.gamera.dto.comment.CommentSlimDto;
import com.avengers.gamera.dto.game.GameSlimGetDto;
import com.avengers.gamera.dto.tag.TagSlimDto;
import com.avengers.gamera.dto.user.UserProfileDto;
import com.avengers.gamera.entity.Article;
import com.avengers.gamera.entity.Comment;
import com.avengers.gamera.entity.Game;
import com.avengers.gamera.entity.Tag;
import com.avengers.gamera.entity.User;
import com.avengers.gamera.exception.ArgumentNotValidException;
import com.avengers.gamera.exception.GameraAccessDeniedException;
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.avengers.gamera.mapper.ArticleMapper;
import com.avengers.gamera.mapper.CommentMapper;
import com.avengers.gamera.mapper.TagMapper;
import com.avengers.gamera.mapper.UserMapper;
import com.avengers.gamera.repository.ArticleRepository;
import com.avengers.gamera.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private final ChatGptService chatGptService;
    private final CommentRepository commentRepository;

    public PagingDto<List<MiniArticleGetDto>> getArticlePage(EArticleType articleType,
                                                             int page,
                                                             int size,
                                                             String platform,
                                                             String genre,
                                                             EArticleSort sort,
                                                             Sort.Direction order) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(order, sort.getName()));

        Page<Article> articlePage = platform.equals("all") & genre.equals("all")
                ? articleRepository.findArticlesByTypeAndIsDeletedFalse(articleType, pageable)
                : articleRepository.findArticlesByTypeAndPlatformAndGenreAndIsDeletedFalse(articleType, platform, genre, pageable);

        List<MiniArticleGetDto> miniArticleGetDtoList = articlePage.getContent()
                .stream()
                .map(articleMapper::articleToMiniArticleGetDto)
                .toList();

        return PagingDto.<List<MiniArticleGetDto>>builder()
                .data(miniArticleGetDtoList)
                .currentPage(articlePage.getNumber() + 1)
                .totalPages(articlePage.getTotalPages())
                .totalItems(articlePage.getTotalElements())
                .build();
    }

    public ArticleGetDto createArticle(ArticlePostDto articlePostDto, EArticleType articleType, Long userId) {
        if (articleType == EArticleType.REVIEW && articlePostDto.getGameId() == null) {
            throw new ArgumentNotValidException();
        }

        return handleCreateArticle(articlePostDto, articleType, userId);

    }

    public ArticleGetDto handleCreateArticle(ArticlePostDto articlePostDto, EArticleType articleType, Long userId) {
        if (articlePostDto.getTagList() != null) {
            List<Tag> updateTagList = handleFrontendTagList(articlePostDto.getTagList());
            articlePostDto.setTagList(updateTagList);
        }

        Article article = articleMapper.articlePostDtoToArticle(articlePostDto);
        if (articlePostDto.getGameId() != null) {
            article.setGame(gameService.findActiveGame(articlePostDto.getGameId()));
        }

        article.setType(articleType);
        article.setAuthor(userService.findUser(userId));

        log.info("Saving the article with title:  " + article.getTitle() + "  to database");

        return articleMapper.articleToArticleGetDto(articleRepository.save(article));
    }


    public List<Tag> handleFrontendTagList(List<Tag> tagList) {
        Map<Boolean, List<Tag>> checkTags = tagList.stream()
                .collect(Collectors.partitioningBy(item -> item.getId() == null));
        List<Tag> newTagFromUser = checkTags.get(true);
        List<Tag> existTagFromUser = checkTags.get(false);
        List<Tag> existTag = tagService.getExistTag(existTagFromUser);
        List<Tag> updatedTagList = new ArrayList<>(existTag);

        if (newTagFromUser.size() > 0) {
            List<Tag> createdTag = tagService.createMultipleTag(newTagFromUser);
            updatedTagList.addAll(createdTag);
        }

        return updatedTagList;
    }

    public Article findById(Long articleId) {
        return articleRepository.findArticleByIdAndIsDeletedFalse(articleId).orElseThrow(() ->
                new ResourceNotFoundException("Related Article with the ID(" + articleId + ")")
        );
    }

    public ArticleGetDto getArticleById(Long articleId, Long currentLoggedInUserId) {
        Article article = findById(articleId);
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
        if (currentLoggedInUserId != null) {
            List<Long> likeUsersId = article.getLikeUsers().stream().map(User::getId).toList();
            articleGetDto.setCurrentUserLiked(likeUsersId.contains(currentLoggedInUserId));
        }
        //For LikeNum
        articleGetDto.setLikeNum(likeService.getLikeNumByArticleId(articleId));
        return articleGetDto;
    }

    public String deleteArticleById(Long articleId, Long currentLoggedInUserId) {
        Article article = articleRepository.findArticleByIdAndIsDeletedFalse(articleId).orElseThrow(() ->
                new ResourceNotFoundException("Related Article with the ID(" + articleId + ")")
        );
        if (!Objects.equals(currentLoggedInUserId, article.getAuthor().getId())){
            throw new GameraAccessDeniedException();
        }
        log.info("Article with ID(" + articleId + ") title(" + article.getTitle() + ") is being deleted");
        article.setDeleted(true);
        articleRepository.save(article);
        return "The article with ID(" + articleId + ") has been deleted";
    }

    public ArticleGetDto updateArticle(ArticlePutDto articlePutDto, Long articleId, Long currentLoggedInUserId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ResourceNotFoundException("Article", articleId));
        if (!Objects.equals(currentLoggedInUserId, article.getAuthor().getId())){
            throw new GameraAccessDeniedException();
        }
        article.setTitle(articlePutDto.getTitle());
        article.setText(articlePutDto.getText());
        article.setUpdatedTime(OffsetDateTime.now());

        log.info("Updated article with id " + articleId + " in the database.");
        return articleMapper.articleToArticleGetDto(articleRepository.save(article));
    }

    public PagingDto<List<MiniArticleGetDto>> getPopularReviewArticlesByCommentNum(int page, int size, EArticleType articleType) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Article> getAllReviewsByCommentNumDesc = articleRepository.findArticlesByTypeAndIsDeletedFalseOrderByCommentNumDesc(articleType, pageable);

        List<MiniArticleGetDto> miniArticleGetDtoList = getAllReviewsByCommentNumDesc.getContent()
                .stream()
                .map(articleMapper::articleToMiniArticleGetDto)
                .toList();

        return PagingDto.<List<MiniArticleGetDto>>builder()
                .data(miniArticleGetDtoList)
                .currentPage(getAllReviewsByCommentNumDesc.getNumber() + 1)
                .totalPages(getAllReviewsByCommentNumDesc.getTotalPages())
                .totalItems(getAllReviewsByCommentNumDesc.getTotalElements())
                .build();
    }

    public PagingDto<List<MiniArticleGetDto>> getArticlesOrderByLike(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Article> allByOrderByLikeNumDesc = articleRepository.findAllByOrderByLikeNumDesc(pageable);

        List<MiniArticleGetDto> miniArticleGetDtoList = allByOrderByLikeNumDesc.getContent().stream()
                .map(articleMapper::articleToMiniArticleGetDto)
                .collect(Collectors.toList());

        return PagingDto.<List<MiniArticleGetDto>>builder()
                .data(miniArticleGetDtoList)
                .currentPage(allByOrderByLikeNumDesc.getNumber() + 1)
                .totalPages(allByOrderByLikeNumDesc.getTotalPages())
                .totalItems(allByOrderByLikeNumDesc.getTotalElements())
                .build();
    }

    public PagingDto<List<MiniArticleGetDto>> getArticlesByAuthorId(int page, int size, Long authorId) {
        Pageable pageable = PageRequest.of(page - 1, size);

        User author = userService.findUser(authorId);

        Page<Article> postedArticles = articleRepository.findArticlesByAuthor(author, pageable);

        List<MiniArticleGetDto> miniArticleByAuthor = postedArticles.getContent()
                .stream()
                .map(articleMapper::articleToMiniArticleGetDto)
                .toList();

        return PagingDto.<List<MiniArticleGetDto>>builder()
                .data(miniArticleByAuthor)
                .currentPage(postedArticles.getNumber() + 1)
                .totalPages(postedArticles.getTotalPages())
                .totalItems(postedArticles.getTotalElements())
                .build();
    }

    public PagingDto<List<MiniArticleGetDto>> getArticlesByCommentUserId(int page, int size, Long commentUserId) {
        Pageable pageable = PageRequest.of(page - 1, size);

        userService.findUser(commentUserId);

        Page<Article> commentedArticles = articleRepository.findArticlesByCommentedUser(commentUserId, pageable);

        List<MiniArticleGetDto> miniArticleByCommentedUser = commentedArticles.getContent()
                .stream()
                .map(articleMapper::articleToMiniArticleGetDto)
                .toList();

        return PagingDto.<List<MiniArticleGetDto>>builder()
                .data(miniArticleByCommentedUser)
                .currentPage(commentedArticles.getNumber() + 1)
                .totalPages(commentedArticles.getTotalPages())
                .totalItems(commentedArticles.getTotalElements())
                .build();
    }

    public PagingDto<List<MiniArticleGetDto>> getArticlesByLikeUserId(int page, int size, Long likeUserId) {
        Pageable pageable = PageRequest.of(page - 1, size);

        userService.findUser(likeUserId);

        Page<Article> likedArticles = articleRepository.findArticlesByLikedUser(likeUserId, pageable);

        List<MiniArticleGetDto> miniArticleByLikedUser = likedArticles.getContent()
                .stream()
                .map(articleMapper::articleToMiniArticleGetDto)
                .toList();

        return PagingDto.<List<MiniArticleGetDto>>builder()
                .data(miniArticleByLikedUser)
                .currentPage(likedArticles.getNumber() + 1)
                .totalPages(likedArticles.getTotalPages())
                .totalItems(likedArticles.getTotalElements())
                .build();
    }

    public UserProfileDto getUserArticleNumAndRecent3MiniArticlesForProfile(Long userId) {
        int articleNums = 3;
        UserProfileDto userProfileDto = new UserProfileDto();
        User user = userService.findUser(userId);
        userProfileDto.setLikesArticlesDto(getRecentMiniArticlesByUser(user, EUserArticleType.LIKES, articleNums));
        userProfileDto.setCommentsArticlesDto(getRecentMiniArticlesByUser(user, EUserArticleType.COMMENTS, articleNums));
        if (user.getAuthorities().stream().anyMatch(authority -> authority.getName().contains("ROLE_EDITOR"))) {
            userProfileDto.setPostsArticlesDto(getRecentMiniArticlesByUser(user, EUserArticleType.POSTS, 3));
            userProfileDto.setPostsCount(articleRepository.countByAuthorIdAndIsDeletedFalse(userId));
        }
        userProfileDto.setLikesCount(user.getLikedArticles().size());
        userProfileDto.setCommentsCount(commentRepository.countByUserIdAndIsDeletedFalse(userId));
        return userProfileDto;
    }

    private List<MiniArticleGetDto> getRecentMiniArticlesByUser(User user, EUserArticleType userArticleType, Integer articleNums) {
        switch (userArticleType) {
            case LIKES:
                return user.getLikedArticles().stream()
                        .map(articleMapper::articleToMiniArticleGetDto).sorted(Comparator.comparing(MiniArticleGetDto::getCreatedTime).reversed()).limit(articleNums).toList();
            case COMMENTS:
                return articleRepository.findAllByIdIn(this.getNewestThreeCommentedArticleIdByUserId(user.getId())).stream()
                        .map(articleMapper::articleToMiniArticleGetDto).toList();
            case POSTS:
                return this.getNewestThreeArticlesByUserId(user.getId()).stream()
                        .map(articleMapper::articleToMiniArticleGetDto).toList();
            default:
                return new ArrayList<>();
        }
    }

    private List<Article> getNewestThreeArticlesByUserId(Long userId) {
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdTime"));
        return articleRepository.findTopNewestArticlesByAuthorId(userId, pageable);
    }

    private List<Long> getNewestThreeCommentedArticleIdByUserId(Long userId) {
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdTime"));
        return commentRepository.findNewestArticleIdsByUserId(userId, pageable);
    }

    public List<ArticleGetDto> getFirstTenNewsByCreatedTime() {
        List<Article> articleList = articleRepository.findFirst10ByTypeAndIsDeletedFalseOrderByCreatedTimeAsc(EArticleType.NEWS);

        return articleList.stream().map(articleMapper::articleToArticleGetDto).toList();
    }

    public ArticleGetDto createByChatGpt() {
        GameSlimGetDto gameSlimGetDto = gameService.getRandomGame();

        String gameName = gameSlimGetDto.getName();
        String promptTemplate = "I want you to be a game review editor. You can write a game review within 1000 words " +
                "about the game \"" + gameName + "\". You must start the review with \"The review title is: \" which must be wrapped in a <h1> tag. " +
                "You can have multiple subtitles and paragraph, but you must wrap subtitle into html <h2> tag and must wrap each paragraph into <p> tag.";

        String response = chatGptService.getChat(promptTemplate);

        String title = StringUtils.substringBetween(response, "The review title is: ", "<");
        String text = StringUtils.substringAfter(response, "</h1>");

        Article article = Article.builder()
                .title(title)
                .text(text)
                .game(Game.builder().id(gameSlimGetDto.getId()).build())
                .type(EArticleType.REVIEW)
                .author(userService.getByEmail("chatgpt@gamera.com.au"))
                .coverImgUrl(gameSlimGetDto.getImgUrl())
                .build();

        log.info("Saving article created by chat gpt. {}", article.getTitle());

        return articleMapper.articleToArticleGetDto(articleRepository.save(article));
    }
}
