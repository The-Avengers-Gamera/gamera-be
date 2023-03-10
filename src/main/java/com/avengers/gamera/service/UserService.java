package com.avengers.gamera.service;

import com.avengers.gamera.dto.user.UserGetDto;
import com.avengers.gamera.dto.user.UserInfoDto;
import com.avengers.gamera.dto.user.UserPostDto;
import com.avengers.gamera.dto.user.UserPutDto;
import com.avengers.gamera.entity.Authority;
import com.avengers.gamera.entity.User;
import com.avengers.gamera.exception.ResourceExistException;
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.avengers.gamera.mapper.UserMapper;
import com.avengers.gamera.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthorityService authorityService;

    @Autowired
    PasswordEncoder passwordEncoder;

    private String defaultAuthority = "ROLE_USER";

    public UserGetDto createUser(UserPostDto userPostDto) {
        String encodedPwd = passwordEncoder.encode(userPostDto.getPassword());
        User user = userMapper.userPostDtoToUser(userPostDto);
        user.setPassword(encodedPwd);
        Set<Authority> authorities = new HashSet<>();
        Authority authority = authorityService.getByAuthorityName(defaultAuthority);
        authorities.add(authority);
        user.setAuthorities(authorities);
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

    public String addAuthorityToUser(String email, String name) {
        User user = getByEmail(email);
        Authority authority = authorityService.getByAuthorityName(name);
        log.info("Adding authority {} to user {}", name, email);
        user.getAuthorities().add(authority);
        return "Successfully added authority-" + name + " to user-" + email;
    }

    public List<UserGetDto> getAllUsers() {
        return userRepository.findAllByIsDeletedFalse().stream().map(userMapper::userToUserGetDto).collect(Collectors.toList());
    }

    public UserGetDto getUser(Long userId) {
        return userMapper.userToUserGetDto(findUser(userId));
    }

    public User findUser(Long userId) {
        return userRepository.findUserByIdAndIsDeletedFalse(userId).orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }

    public UserGetDto updateUser(UserPutDto userPutDto, Long userId) {
        User user = findUser(userId);
        user.setName(userPutDto.getName());
        user.setEmail(userPutDto.getEmail());
        user.setPassword(userPutDto.getPassword());
        log.info(" User id {} was updated", userId);
        return userMapper.userToUserGetDto(userRepository.save(user));
    }

    public void deleteUser(Long userId) {
        User user = findUser(userId);
        user.setIsDeleted(true);
        log.info(" User id {} was deleted", userId);
    }

}
