package com.avengers.gamera.controller;

import com.avengers.gamera.dto.authority.AuthorityGetDto;
import com.avengers.gamera.entity.Authority;
import com.avengers.gamera.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("authorities")
@RequiredArgsConstructor
public class AuthorityController {
    private final AuthorityService authorityService;

    @GetMapping
    public List<Authority> getAuthorityList(){
        return authorityService.getAllAuthorities();
    }

    @GetMapping("/{authorityId}")
    public AuthorityGetDto getAuthorityById(@PathVariable Long authorityId) {

        return authorityService.getAuthorityById(authorityId);
    }
    



}
