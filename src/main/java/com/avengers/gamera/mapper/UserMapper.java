package com.avengers.gamera.mapper;

import com.avengers.gamera.dto.user.UserGetDto;
import com.avengers.gamera.dto.user.UserInfoDto;
import com.avengers.gamera.dto.user.UserPostDto;
import com.avengers.gamera.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User userPostDtoToUser(UserPostDto userPostDto);

    UserGetDto userToUserGetDto(User user);

    UserInfoDto userToUserInfoDto(User user);

}
