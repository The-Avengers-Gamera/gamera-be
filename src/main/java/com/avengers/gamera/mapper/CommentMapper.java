package com.avengers.gamera.mapper;

import com.avengers.gamera.dto.comment.CommentGetDto;
import com.avengers.gamera.dto.comment.CommentPostDto;
import com.avengers.gamera.dto.comment.CommentSlimDto;
import com.avengers.gamera.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    CommentGetDto commentToCommentGetDto(Comment comment);

    Comment commentPostDtoToComment(CommentPostDto commentPostDto);

    CommentSlimDto commentToCommentSlimGetDto(Comment comment);
}
