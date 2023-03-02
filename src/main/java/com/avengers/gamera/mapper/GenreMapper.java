package com.avengers.gamera.mapper;

import com.avengers.gamera.dto.genre.GenreGetDto;
import com.avengers.gamera.dto.genre.GenrePostDto;
import com.avengers.gamera.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GenreMapper {
    Genre GenrePostDtoToGenre(GenrePostDto genrePostDto);

    GenreGetDto GenreToGenreGetDto(Genre genre);
}
