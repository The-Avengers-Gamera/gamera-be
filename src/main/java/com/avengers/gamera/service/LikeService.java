package com.avengers.gamera.service;

import com.avengers.gamera.auth.GameraUserDetails;
import com.avengers.gamera.dto.like.LikeGetDto;
import com.avengers.gamera.dto.like.LikeGetForUserProfileDto;
import com.avengers.gamera.dto.like.LikePostDto;
import com.avengers.gamera.entity.Like;
import com.avengers.gamera.exception.ResourceExistException;
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.avengers.gamera.mapper.LikeMapper;
import com.avengers.gamera.repository.ArticleRepository;
import com.avengers.gamera.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final LikeMapper likeMapper;
    private final LikeRepository likeRepository;
    private final ArticleRepository articleRepository;

    public Long getUserId() {
        Long userId;
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof GameraUserDetails userDetails) {
            userId = userDetails.getId();
            return userId;
        }
        throw new RuntimeException("userId can not be found");
    }

    @Transactional
    public LikeGetDto createLike(LikePostDto likePostDto) {
        Like like = likeMapper.likePostDtoToLike(likePostDto);
        if (likeRepository.existsByUserIdAndArticleId(like.getUserId(), like.getArticleId())){
            log.error("user {} already liked article {}",like.getUserId(),like.getArticleId());
            throw new ResourceExistException();
        }
        log.info("Successfully create new like: userId {} articleId {}",like.getUserId(),like.getArticleId());
        return likeMapper.likeToLikeGetDto(likeRepository.save(like));
    }

    @Transactional
    public LikeGetDto getLike(Long articleId) {

        return likeMapper.likeToLikeGetDto(likeRepository.findByUserIdAndArticleId(this.getUserId(), articleId).orElseThrow(() -> new ResourceNotFoundException("like")));
    }

    @Transactional
    public List<LikeGetForUserProfileDto> getLikeByUserId() {
        return (likeRepository.findByUserId(this.getUserId()).stream().map(like -> {
            LikeGetForUserProfileDto likeGetForUserProfileDto = likeMapper.likeToLikeGetForUserProfileDto(like);
            String articleTitle = articleRepository.findTitleByIdAndIsDeletedFalse(likeGetForUserProfileDto.getArticleId());
            likeGetForUserProfileDto.setArticleTile(articleTitle);
            if(articleTitle.isBlank())
                likeGetForUserProfileDto.setIsArticleDeleted(true);
            else
                likeGetForUserProfileDto.setIsArticleDeleted(false);
            return likeGetForUserProfileDto;
        }).sorted(Comparator.comparing(LikeGetForUserProfileDto::getCreatedTime).reversed()).collect(Collectors.toList()));
    }

    @Transactional
    public Long getLikeNumByArticleId(Long articleId) {
        return likeRepository.countByArticleId(articleId);
    }

    public void deleteLike(Long articleId) {
        likeRepository.deleteByUserIdAndArticleId(this.getUserId(), articleId);
        log.info("Delete like with userId {} articleId {}", this.getUserId(), articleId);
        return;
    }
}
