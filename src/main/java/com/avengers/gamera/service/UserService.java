package com.avengers.gamera.service;

import com.avengers.gamera.dto.user.UserGetDto;
import com.avengers.gamera.dto.user.UserInfoDto;
import com.avengers.gamera.dto.user.UserPostDto;
import com.avengers.gamera.entity.Authority;
import com.avengers.gamera.entity.User;
import com.avengers.gamera.exception.ResourceExistException;
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.avengers.gamera.mapper.UserMapper;
import com.avengers.gamera.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final AuthorityService authorityService;

    public UserGetDto createUser(UserPostDto userPostDto) {
        String email = userPostDto.getEmail();

        emailExists(email);
        User user = userMapper.userPostDtoToUser(userPostDto);
        log.info("Saving new user {} to database", user.getEmail());
        return userMapper.userToUserGetDto(userRepository.save(user));
    }

    public Boolean emailExists(String email) {
        Boolean isExisted = userRepository.existsUserByEmail(email);
        if (Boolean.TRUE.equals(isExisted)) {
            throw new ResourceExistException("Email already existed!");
        }
        return isExisted;
    }

    public UserInfoDto getUserInfo(String email) {
        return userMapper.userToUserInfoDto(getByEmail(email));
    }

    private User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", email));
    }

    public void addRoleToUser(String email, String name) {
        User user = getByEmail(email);
        Authority authority = authorityService.getByAuthorityName(name);
        log.info("Adding authority {} to user {}", email, name);
        user.getAuthorities().add(authority);
    }


}
