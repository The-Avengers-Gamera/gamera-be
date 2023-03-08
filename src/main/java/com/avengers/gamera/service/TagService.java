package com.avengers.gamera.service;

import com.avengers.gamera.dto.tag.TagGetDto;
import com.avengers.gamera.dto.tag.TagPostDto;
import com.avengers.gamera.dto.tag.TagPutDto;
import com.avengers.gamera.entity.Tag;
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.avengers.gamera.mapper.TagMapper;
import com.avengers.gamera.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public TagGetDto createTag(TagPostDto tagPostDto) {
        Tag tag = tagMapper.tagPostDtoToTag(tagPostDto);
        return tagMapper.tagToTagGetDto(tagRepository.save(tag));
    }

    public List<Tag> createMultipleTag(List<TagPostDto> tagPostDtoList) {
        List<Tag> tagList = tagPostDtoList.stream().map(tagMapper::tagPostDtoToTag).toList();
        return tagRepository.saveAll(tagList);
    }

    public Tag getTag(Long tagId) {
        return findTag(tagId);
    }

    public List<Tag> saveAllTag(List<Tag> tagNames) {
        List<TagPostDto> tagPostDtoList = tagNames.stream().map(tagMapper::tagToTagPostDto).toList();
        return createMultipleTag(tagPostDtoList);
    }

    public List<Tag> getAllTag(List<Tag> tagNames) {
        List<Long> tagIdList = tagNames.stream().map(Tag::getId).toList();
        return tagRepository.findAllById(tagIdList);
    }

    public Tag updateTag(Long tagId, TagPutDto tagPutDto) {
        Tag tag = findTag(tagId);
        tag.setName(tagPutDto.getName());
        return tagRepository.save(tag);
    }

    public Tag findTag(Long tagId) {
        return tagRepository.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("Tag"));
    }

    @Modifying
    public void deleteTag(Long tagId) {
        int deleteResponse = tagRepository.deleteTagById(tagId);
        if (deleteResponse != 1L) {
            throw new ResourceNotFoundException("Tag", tagId);
        }
    }

}