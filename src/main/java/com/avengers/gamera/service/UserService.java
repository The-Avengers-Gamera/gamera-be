package com.avengers.gamera.service;

import com.avengers.gamera.auth.GameraUserDetails;
import com.avengers.gamera.dto.user.UserGetDto;
import com.avengers.gamera.dto.user.UserPostDto;
import com.avengers.gamera.dto.user.UserPutDto;
import com.avengers.gamera.entity.Authority;
import com.avengers.gamera.entity.User;
import com.avengers.gamera.exception.GameraAccessDeniedException;
import com.avengers.gamera.exception.EmailAddressException;
import com.avengers.gamera.exception.ResourceExistException;
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.avengers.gamera.mapper.UserMapper;
import com.avengers.gamera.repository.UserRepository;
import com.avengers.gamera.service.EService.EmailService;
import com.avengers.gamera.util.SystemParam;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.security.Key;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthorityService authorityService;
    private final SystemParam systemParam;
    private final EmailService emailService;

    private final SecretKey secretKey;

    private final PasswordEncoder passwordEncoder;

    private final String defaultAuthority = "ROLE_USER";


    public UserGetDto createUser(UserPostDto userPostDto) {
        String encodedPwd = passwordEncoder.encode(userPostDto.getPassword());
        User user = userMapper.userPostDtoToUser(userPostDto);
        user.setPassword(encodedPwd);
        Set<Authority> authorities = new HashSet<>();
        Authority authority = authorityService.getByAuthorityName(defaultAuthority);
        authorities.add(authority);
        user.setAuthorities(authorities);
        log.info("Saving new user {} to database", user.getEmail());

        User savedUser=userRepository.save(user);
        sendEmail(savedUser.getEmail(),savedUser.getId(), secretKey);
        return userMapper.userToUserGetDto(savedUser);
    }

    public void sendEmail( String email, Long userId, Key secretKey) {

        String registerLink = createSignUpLink(systemParam.getBaseUrl(), email,userId,secretKey);
        String info = "Active your account";

        try {
            emailService.sendEmail(email, registerLink, info);
        } catch (Exception e) {
            throw new EmailAddressException();
        }
    }

    public String createSignUpLink(String baseUrl, String email, Long userId, Key secretKey ) {

        String jwtToken = Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                .signWith(secretKey)
                .compact();
        return baseUrl + "/verification?code="+ jwtToken;
    }

    public void emailExists(String email) {
        boolean isExisted = userRepository.existsUserByEmail(email);
        if (isExisted== Boolean.TRUE) {
            throw new ResourceExistException("Email already existed!");
        }
    }

    public UserGetDto getUserInfoByToken() {
        UserGetDto user;
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!Objects.equals(userDetails.toString(), "anonymousUser")) {
            Long userId = ((GameraUserDetails) userDetails).getId();
            user = getUser(userId);
        } else {
            throw new GameraAccessDeniedException();
        }
        return user;
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
        String encodedPwd = passwordEncoder.encode(userPutDto.getPassword());
        user.setPassword(encodedPwd);
        log.info(" User id {} was updated", userId);
        return userMapper.userToUserGetDto(userRepository.save(user));
    }

    public void deleteUser(Long userId) {
        User user = findUser(userId);
        user.setIsDeleted(true);
        log.info(" User id {} was deleted", userId);
    }
}
