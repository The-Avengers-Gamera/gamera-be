package com.avengers.gamera.controller;

import com.avengers.gamera.dto.comment.CommentGetDto;
import com.avengers.gamera.dto.comment.CommentPostDto;
import com.avengers.gamera.dto.comment.CommentPutDto;
import com.avengers.gamera.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@CrossOrigin
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentGetDto createComment(@Valid @RequestBody CommentPostDto commentPostDto) {
        return commentService.createNewComment(commentPostDto);
    }

    @GetMapping("/{commentId}")
    public Map<String, Object> get(@PathVariable Long commentId) {
        return commentService.getCommentByCommentId(commentId);
    }

    @PutMapping("/{commentId}")
    public CommentGetDto update(@PathVariable Long commentId, @RequestBody CommentPutDto commentPutDto) {
        return commentService.updateComment(commentId, commentPutDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }
}
