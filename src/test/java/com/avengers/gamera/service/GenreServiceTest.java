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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    GenrePostDto mockGenrePostDto = GenrePostDto.builder().name("Avenger").build();
    GenreGetDto mockGenreGetDto = GenreGetDto.builder().id(1L).name("Avenger").createdTime(OffsetDateTime.now()).updatedTime(OffsetDateTime.now()).build();
    GenreGetDto mockUpdateGenreGetDto = GenreGetDto.builder().id(1L).name("Avenger2").createdTime(OffsetDateTime.now()).updatedTime(OffsetDateTime.now()).build();
    GenreUpdateDto mockGenreUpdateDto = GenreUpdateDto.builder().name("Avenger2").build();

    @Test
    void shouldSaveNewGenreInGenreRepoWhenCreateGenre() {
        when(genreMapper.GenrePostDtoToGenre(mockGenrePostDto)).thenReturn(mockGenre);
        when(genreRepository.save(mockGenre)).thenReturn(mockGenre);
        when(genreMapper.GenreToGenreGetDto(mockGenre)).thenReturn(mockGenreGetDto);

        GenreGetDto genreGetDto = genreService.createGenre(mockGenrePostDto);
        verify(genreRepository).save(mockGenre);
        assertEquals(mockGenreGetDto, genreGetDto);
    }

    @Test
    void shouldReturnGenreGetDtoWhenGet() {
        Long mockId = 1L;
        when(genreRepository.findById(mockId)).thenReturn(Optional.of(mockGenre));
        when(genreMapper.GenreToGenreGetDto(mockGenre)).thenReturn(mockGenreGetDto);

        GenreGetDto genreGetDto = genreService.getGenre(mockId);
        assertEquals(mockGenreGetDto, genreGetDto);

    }

    @Test
    void shouldReturnGenreGetDtoWhenUpdateGenre() {
        Long mockId = 1L;
        when(genreRepository.findById(mockId)).thenReturn(Optional.ofNullable(mockGenre));
        when(genreMapper.GenreToGenreGetDto(mockGenre)).thenReturn(mockUpdateGenreGetDto);

        GenreGetDto genreGetDto = genreService.updateGe(mockGenreUpdateDto, mockId);

        assertEquals(mockUpdateGenreGetDto, genreGetDto);

    }

}
