package com.avengers.gamera.controller;

import com.avengers.gamera.dto.PagingDto;
import com.avengers.gamera.dto.article.MiniArticleGetDto;
import com.avengers.gamera.dto.user.UserAddAuthorityDto;
import com.avengers.gamera.dto.user.UserGetDto;
import com.avengers.gamera.dto.user.UserPostDto;
import com.avengers.gamera.dto.user.UserPutDto;
import com.avengers.gamera.service.ArticleService;
import com.avengers.gamera.dto.user.*;
import com.avengers.gamera.service.UserService;
import com.avengers.gamera.util.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ArticleService articleService;

    @PostMapping("/signup")
    @Operation(summary = "Create new user")
    public UserGetDto createUser(@Valid @RequestBody UserPostDto userPostDto) {
        return userService.createUser(userPostDto);
    }

    @PostMapping("/roles")
    @ResponseStatus(HttpStatus.CREATED)
    public String addAuthorityToUser(@RequestBody UserAddAuthorityDto userAddAuthority) {
        return userService.addAuthorityToUser(userAddAuthority.getEmail(), userAddAuthority.getName());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public List<UserGetDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("{userId}")
    public UserGetDto getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/info")
    public UserGetDto getUserInfo() {
        return userService.getUserInfoByToken();
    }

    @PutMapping
    public UserGetDto updateUser(@Valid @RequestBody UserPutDto userPutDto) {
        Long currentUserId = CurrentUser.getUserId();
        return userService.updateUser(userPutDto, currentUserId);
    }

    @DeleteMapping("{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    @GetMapping("/verification")
    public void getEmailExists (@RequestParam String email){
        userService.emailExists(email);
    }

    @GetMapping("/{userId}/likes")
    public PagingDto<List<MiniArticleGetDto>> getLikedListByUser(@RequestParam(defaultValue = "1") int page,
                                                                 @RequestParam(defaultValue = "10") int size,
                                                                 @PathVariable Long userId) {
        return articleService.getArticlesByLikeUserId(page, size, userId);
    }

    @GetMapping("/{userId}/comments")
    public PagingDto<List<MiniArticleGetDto>> getCommentedListByUser(@RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @PathVariable Long userId) {
        return articleService.getArticlesByCommentUserId(page, size, userId);
    }

    @GetMapping("/{userId}/articles")
    public PagingDto<List<MiniArticleGetDto>> getArticlesListByAuthor(@RequestParam(defaultValue = "1") int page,
                                                                      @RequestParam(defaultValue = "10") int size,
                                                                      @PathVariable Long userId) {
        return articleService.getArticlesByAuthorId(page, size, userId);
    }
    @GetMapping("/{userId}/profile")
    public UserProfileDto getUserProfile(@PathVariable Long userId) {
        UserProfileDto userProfileDto = articleService.getUserArticleNumAndRecent3MiniArticlesForProfile(userId);
        userProfileDto.setFromUserGetDto(userService.getUser(userId));
        return userProfileDto;
    }
}
