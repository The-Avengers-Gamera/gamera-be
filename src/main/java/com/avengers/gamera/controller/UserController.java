package com.avengers.gamera.controller;

import com.avengers.gamera.dto.PagingDto;
import com.avengers.gamera.dto.article.ArticleGetDto;
import com.avengers.gamera.dto.article.MiniArticleGetDto;
import com.avengers.gamera.dto.comment.CommentGetDto;
import com.avengers.gamera.dto.user.UserAddAuthorityDto;
import com.avengers.gamera.dto.user.UserGetDto;
import com.avengers.gamera.dto.user.UserPostDto;
import com.avengers.gamera.dto.user.UserPutDto;
import com.avengers.gamera.service.ArticleService;
import com.avengers.gamera.service.LikeService;
import com.avengers.gamera.service.UserService;
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
    private final LikeService likeService;
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

    @GetMapping
    public List<UserGetDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("{userId}")
    public UserGetDto getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/info")
    public UserGetDto getUserInfo() {
        return userService.getUserInfoByToken();
    }

    @PutMapping("{userId}")
    public UserGetDto updateUser(@Valid @RequestBody UserPutDto userPutDto, @PathVariable Long userId) {
        return userService.updateUser(userPutDto, userId);
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

    @GetMapping("/likes")
    public List<ArticleGetDto> getLikeListForUser() {
        return likeService.getLikeByUserId();
    }

    @GetMapping("/comment")
    public List<CommentGetDto> getCommentedForUser() {
        return likeService.getCommented();
    }

    @GetMapping("/{userId}/articles")
    public PagingDto<List<MiniArticleGetDto>> getArticlesListByAuthor(@RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @PathVariable Long userId) {
        return articleService.getArticlesByAuthorId(page, size, userId);
    }
}
