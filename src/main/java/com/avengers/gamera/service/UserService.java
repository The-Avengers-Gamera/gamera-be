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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final AuthorityService authorityService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserGetDto createUser(UserPostDto userPostDto) {
        String encodedPwd =  passwordEncoder.encode(userPostDto.getPassword());
        String email = userPostDto.getEmail();
        // verify if the email exists
        emailExists(email);
        // save encoded password
        User user = userMapper.userPostDtoToUser(userPostDto);
        user.setPassword(encodedPwd);
        // add authority to user
        // one user can hava many authorities
        Set<Authority> authorities = new HashSet<>();
        Authority authority = authorityService.getByAuthorityName("Role_User");
        authorities.add(authority);
        user.setAuthorities(authorities);
        log.info("Saving new user {} to database, with pwd: {}", user.getEmail(), user.getPassword());
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

    public void addAuthorityToUser(String email, String name) {
        User user = getByEmail(email);
        Authority authority = authorityService.getByAuthorityName(name);
        log.info("Adding authority {} to user {}", email, name);
        user.getAuthorities().add(authority);
    }

    public List<UserGetDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::userToUserGetDto).collect(Collectors.toList());
    }

    public UserGetDto getUser(Long userId) {
        return userMapper.userToUserGetDto(findUser(userId));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", userId));
    }

    public UserGetDto updateUser(UserPutDto userPutDto, Long userId) {
        User user = findUser(userId);
        user.setName(userPutDto.getName());
        user.setEmail(userPutDto.getEmail());
        user.setPassword(userPutDto.getPassword());
        return userMapper.userToUserGetDto(userRepository.save(user));
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }


}
