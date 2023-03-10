package com.avengers.gamera.service;

import com.avengers.gamera.dto.authority.AuthorityGetDto;
import com.avengers.gamera.entity.Authority;
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.avengers.gamera.mapper.AuthorityMapper;
import com.avengers.gamera.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    private final AuthorityMapper authorityMapper;

    public Authority getByAuthorityName(String authorityName) {
        return authorityRepository.findByName(authorityName).orElseThrow(() -> new ResourceNotFoundException("Authority", authorityName));
    }

    public List<AuthorityGetDto> getAllAuthorities() {
        List<AuthorityGetDto> authorityGetDtoList = authorityRepository.findAll().stream().map(authorityMapper::authorityToAuthorityGetDto).toList();
        return authorityGetDtoList;
    }

    public AuthorityGetDto getAuthorityById(Long authorityId) {
        return authorityMapper.authorityToAuthorityGetDto(authorityRepository.findById(authorityId)
                .orElseThrow(() -> new ResourceNotFoundException("Authority", authorityId)));
    }

}
