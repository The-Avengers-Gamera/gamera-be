package com.avengers.gamera.service;

import com.avengers.gamera.dto.genre.GenrePostDto;
import com.avengers.gamera.dto.genre.GenreUpdateDto;
import com.avengers.gamera.entity.Genre;
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.avengers.gamera.mapper.GenreMapper;
import com.avengers.gamera.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public Genre createGenre(GenrePostDto genrePostDto) {
        Genre genre = genreMapper.GenrePostDtoToGenre(genrePostDto);
        return genreRepository.save(genre);
    }

    public List<Genre> createMultipleGenre(List<GenrePostDto> genrePostDtoList) {
        List<Genre> genreList = genrePostDtoList.stream().map(genreMapper::GenrePostDtoToGenre).toList();
        return genreRepository.saveAll(genreList);
    }

    public Genre getGenre(Long id) {
        return findGenre(id);
    }

    public List<Genre> saveAllGenre(List<Genre> genreNames) {
        List<GenrePostDto> genrePostDtoList = genreNames.stream().map(genreMapper::GenreToGenrePostDto).toList();
        return createMultipleGenre(genrePostDtoList);
    }

    public List<Genre> getAllGenre(List<Genre> genreNames) {
        List<Long> genreIdList = genreNames.stream().map(Genre::getId).toList();
        return genreRepository.findAllById(genreIdList);
    }

    public Genre updateGe(GenreUpdateDto genreUpdateDto, Long id) {
        Genre genre = findGenre(id);
        genre.setName(genreUpdateDto.getName());
        return genreRepository.save(genre);

    }

    public Genre findGenre(Long id) {
        return genreRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Genre"));
    }

}
