package com.avengers.gamera.service;

import com.avengers.gamera.dto.game.GameGenrePostDto;
import com.avengers.gamera.dto.genre.GenreGetDto;
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

    public GenreGetDto createGenre(GenrePostDto genrePostDto) {
        Genre genre = genreMapper.GenrePostDtoToGenre(genrePostDto);
        return genreMapper.GenreToGenreGetDto(genreRepository.save(genre));
    }

    public List<Genre> createMultipleGenre(List<String> nameList) {

        List<GenrePostDto> genrePostDtoList = nameList.stream().map(item->GenrePostDto.builder().name(item).build()).toList();
        List<Genre> genreList=genrePostDtoList.stream().map(genreMapper::GenrePostDtoToGenre).toList();
        return genreRepository.saveAll(genreList);
    }


    public Genre getGenre(Long id) {
        return findGenre(id);
    }

    public List<Genre> saveAllGenre(List<GameGenrePostDto> genreNames) {
        List<String> genrePostDtoList = genreNames.stream().map(GameGenrePostDto::getName).toList();
        return createMultipleGenre(genrePostDtoList);
    }

    public List<Genre> getAllGenre(List<GameGenrePostDto> genreNames) {
        List<Long> genreIdList = genreNames.stream().map(GameGenrePostDto::getId).toList();
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
