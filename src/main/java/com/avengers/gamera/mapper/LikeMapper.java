package com.avengers.gamera.mapper;

import com.avengers.gamera.dto.like.LikeGetDto;
import com.avengers.gamera.dto.like.LikePostDto;
import com.avengers.gamera.entity.Like;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LikeMapper {
    Like LikePostDtoToLike(LikePostDto likePostDto);

    LikeGetDto LikeToLikeGetDto(Like like);
}
