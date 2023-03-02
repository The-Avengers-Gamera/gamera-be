package com.avengers.gamera.controller;

import com.avengers.gamera.dto.like.LikeGetDto;
import com.avengers.gamera.dto.like.LikePostDto;
import com.avengers.gamera.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("like")
public class LikeController {
    private final LikeService likeService;

    @PostMapping
    @Operation(summary = "Create new like")
    public LikeGetDto createLike(@Valid @RequestBody LikePostDto likePostDto) {
        return likeService.createLike(likePostDto);
    }

    @GetMapping("{userId}&&{articleId}")
    @Operation(summary = "Get a like")
    public LikeGetDto getLike(@PathVariable Long userId,@PathVariable Long articleId) {
        return likeService.getLike(userId, articleId);
    }
}
