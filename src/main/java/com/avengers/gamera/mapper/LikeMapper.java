package com.avengers.gamera.mapper;

import com.avengers.gamera.dto.like.LikeGetDto;
import com.avengers.gamera.dto.like.LikeGetForUserProfileDto;
import com.avengers.gamera.dto.like.LikePostDto;
import com.avengers.gamera.entity.Like;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LikeMapper {
    Like likePostDtoToLike(LikePostDto likePostDto);

    LikeGetDto likeToLikeGetDto(Like like);

    LikeGetForUserProfileDto likeToLikeGetForUserProfileDto(Like like);
}
