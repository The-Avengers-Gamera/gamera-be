package com.avengers.gamera.service;


import com.avengers.gamera.dto.genre.GenreGetDto;
import com.avengers.gamera.dto.genre.GenrePostDto;
import com.avengers.gamera.dto.genre.GenreUpdateDto;
import com.avengers.gamera.entity.Genre;
import com.avengers.gamera.mapper.GenreMapper;
import com.avengers.gamera.repository.GenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private GenreMapper genreMapper;
    @InjectMocks
    private GenreService genreService;

    Genre mockGenre = Genre.builder().id(1L).name("Avenger").createdTime(OffsetDateTime.now()).updatedTime(OffsetDateTime.now()).build();
    GenreGetDto mockGenreGetDto=GenreGetDto.builder().id(1L).name("Avenger").createdTime(OffsetDateTime.now()).updatedTime(OffsetDateTime.now()).build();
    Genre mockGenre3 = Genre.builder().id(1L).name("Avenger1").createdTime(OffsetDateTime.now()).updatedTime(OffsetDateTime.now()).build();
    Genre mockGenre4 = Genre.builder().id(1L).name("Avenger2").createdTime(OffsetDateTime.now()).updatedTime(OffsetDateTime.now()).build();
    GenrePostDto mockGenrePostDto = GenrePostDto.builder().name("Avenger").build();
    GenrePostDto mockGenrePostDto1 = GenrePostDto.builder().name("Avenger1").build();
    GenrePostDto mockGenrePostDto2 = GenrePostDto.builder().name("Avenger2").build();
    GenreUpdateDto mockGenreUpdateDto = GenreUpdateDto.builder().name("Avenger2").build();
    Genre mockGenre1 = Genre.builder().name("Avenger1").build();
    Genre mockGenre2 = Genre.builder().name("Avenger2").build();
    List<Genre> genreNames = List.of(mockGenre1, mockGenre2);

    List<Genre> mockGenreList = List.of(mockGenre3, mockGenre4);

    @Test
    void shouldSaveNewGenreInGenreRepoWhenCreateGenre() {
        when(genreMapper.GenrePostDtoToGenre(mockGenrePostDto)).thenReturn(mockGenre);
        when(genreRepository.save(mockGenre)).thenReturn(mockGenre);
        when(genreMapper.GenreToGenreGetDto(mockGenre)).thenReturn(mockGenreGetDto);

        GenreGetDto genreDto = genreService.createGenre(mockGenrePostDto);
        verify(genreRepository).save(mockGenre);
        assertEquals(mockGenreGetDto, genreDto);
    }

    @Test
    void shouldReturnGenreGetDtoWhenGet() {
        Long mockId = 1L;
        when(genreRepository.findById(mockId)).thenReturn(Optional.of(mockGenre));

        Genre genreDto = genreService.getGenre(mockId);
        assertEquals(mockGenre, genreDto);

    }


    @Test
    void shouldReturnGenreListWhenSaveAll() {
        when(genreMapper.GenreToGenrePostDto(any())).thenReturn(mockGenrePostDto1, mockGenrePostDto2);
        when(genreMapper.GenrePostDtoToGenre(any())).thenReturn(mockGenre3, mockGenre4);
        when(genreRepository.saveAll(any())).thenReturn(mockGenreList);

        List<Genre> genreList = genreService.saveAllGenre(genreNames);
        assertEquals(mockGenreList, genreList);

    }

    @Test
    void shouldReturnGenreWhenUpdateGenre() {
        Long mockId = 1L;
        when(genreRepository.findById(mockId)).thenReturn(Optional.ofNullable(mockGenre));
        when(genreRepository.save(mockGenre)).thenReturn(mockGenre4);

        Genre genreDto = genreService.updateGe(mockGenreUpdateDto, mockId);

        assertEquals(mockGenre4, genreDto);

    }

}
