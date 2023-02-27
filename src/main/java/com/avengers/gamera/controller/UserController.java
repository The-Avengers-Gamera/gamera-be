package com.avengers.gamera.controller;

import com.avengers.gamera.dto.user.UserAddAuthorityDto;
import com.avengers.gamera.dto.user.UserGetDto;
import com.avengers.gamera.dto.user.UserPostDto;
import com.avengers.gamera.dto.user.UserPutDto;
import com.avengers.gamera.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "Create new user")
    public ResponseEntity<UserGetDto> createUser(@Valid @RequestBody UserPostDto userPostDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userPostDto));
    }

    @PostMapping("/add-authority")
    public ResponseEntity<UserAddAuthorityDto> addAuthorityToUser(@RequestBody UserAddAuthorityDto userAddAuthority) {
        userService.addAuthorityToUser(userAddAuthority.getEmail(), userAddAuthority.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<UserGetDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("{userId}")
    public UserGetDto getUser(@PathVariable Long userId){
        return userService.getUser(userId);
    }

    @PostMapping("{userId}")
    public UserGetDto updateUser(@Valid @RequestBody UserPutDto userPutDto, @PathVariable Long userId){
        return userService.updateUser(userPutDto, userId);
    }

    @DeleteMapping("{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
    }

    // user get authority



}
