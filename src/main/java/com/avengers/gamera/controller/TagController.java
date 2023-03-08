package com.avengers.gamera.controller;

import com.avengers.gamera.dto.tag.TagGetDto;
import com.avengers.gamera.dto.tag.TagPostDto;
import com.avengers.gamera.dto.tag.TagPutDto;
import com.avengers.gamera.entity.Tag;
import com.avengers.gamera.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("tags")
public record TagController(TagService tagService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagGetDto createTag(@Valid @RequestBody TagPostDto tagPostDto) {
        return tagService.createTag(tagPostDto);
    }

    @PostMapping("/multiple")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Tag> createTag(@Valid @RequestBody List<Tag> tagNames) {
        return tagService.saveAllTag(tagNames);
    }

    @GetMapping("/multiple")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Tag> getMultipleTag(@Valid @RequestBody List<Tag> tagNames) {
        return tagService.getAllTag(tagNames);
    }

    @GetMapping("/{tagId}")
    public Tag getTag(@PathVariable Long tagId) {
        return tagService.getTag(tagId);
    }

    @PutMapping("/{tagId}")
    public Tag update(@PathVariable Long tagId, @RequestBody TagPutDto tagPutDto) {
        return tagService.updateTag(tagId, tagPutDto);
    }

    @DeleteMapping("/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long tagId) {
        tagService.deleteTag(tagId);
    }

}
