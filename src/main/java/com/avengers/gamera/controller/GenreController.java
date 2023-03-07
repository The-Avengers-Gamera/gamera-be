package com.avengers.gamera.controller;


import com.avengers.gamera.dto.game.GameGenrePostDto;
import com.avengers.gamera.dto.genre.GenrePostDto;
import com.avengers.gamera.dto.genre.GenreUpdateDto;
import com.avengers.gamera.entity.Genre;
import com.avengers.gamera.service.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("genres")
public record GenreController(GenreService genreService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenreGetDto createGenre(@Valid @RequestBody GenrePostDto genrePostDto) {
        return genreService.createGenre(genrePostDto);
    }


    @GetMapping("/multiple")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Genre> getMultipleGenre(@Valid @RequestBody List<GameGenrePostDto> genreNames) {
        return genreService.getAllGenre(genreNames);
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable Long id) {
        return genreService.getGenre(id);
    }

    @PutMapping("/{id}")
    public Genre updateGe(@Valid @RequestBody GenreUpdateDto genreUpdateDto, @PathVariable Long id) {
        return genreService.updateGe(genreUpdateDto, id);
    }


}
