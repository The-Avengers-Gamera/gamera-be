package com.avengers.gamera.service;

import com.avengers.gamera.dto.like.LikeGetDto;
import com.avengers.gamera.dto.like.LikePostDto;
import com.avengers.gamera.entity.Like;
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.avengers.gamera.mapper.LikeMapper;
import com.avengers.gamera.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final LikeMapper likeMapper;
    private final LikeRepository likeRepository;

    @javax.transaction.Transactional
    public LikeGetDto createLike(LikePostDto likePostDto) {
        Like like = likeMapper.LikePostDtoToLike(likePostDto);
        return likeMapper.LikeToLikeGetDto(likeRepository.save(like));
    }

    @Transactional
    public LikeGetDto getLike(Long userId, Long articleId) {
        return likeMapper.LikeToLikeGetDto(likeRepository.findByUserIdAndArticleId(userId, articleId).orElseThrow(() -> new ResourceNotFoundException("like")));
    }
}
