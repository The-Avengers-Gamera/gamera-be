package com.avengers.gamera.controller;

import com.avengers.gamera.dto.genre.GenreGetDto;
import com.avengers.gamera.dto.genre.GenrePostDto;
import com.avengers.gamera.dto.genre.GenreUpdateDto;
import com.avengers.gamera.service.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("genres")
public record GenreController(GenreService genreService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenreGetDto createGenre(@Valid @RequestBody GenrePostDto genrePostDto) {
        return genreService.createGenre(genrePostDto);
    }

    @GetMapping("/{id}")
    public GenreGetDto getGenre(@PathVariable Long id) {
        return genreService.getGenre(id);
    }

    @PutMapping("/{id}")
    public GenreGetDto updateGe(@Valid @RequestBody GenreUpdateDto genreUpdateDto, @PathVariable Long id) {
        return genreService.updateGe(genreUpdateDto, id);
    }


}
