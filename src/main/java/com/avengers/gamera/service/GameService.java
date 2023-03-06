package com.avengers.gamera.service;

import com.avengers.gamera.dto.game.GameGetDto;
import com.avengers.gamera.dto.game.GamePostDto;
import com.avengers.gamera.dto.game.GameUpdateDto;
import com.avengers.gamera.entity.Game;
import com.avengers.gamera.entity.Genre;
import com.avengers.gamera.exception.ResourceExistException;
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.avengers.gamera.mapper.GameMapper;
import com.avengers.gamera.mapper.GenreMapper;
import com.avengers.gamera.repository.GameRepository;
import com.avengers.gamera.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class GameService {
    public final GameRepository gameRepository;
    public final GameMapper gameMapper;
    public final GenreRepository genreRepository;

    public final GenreService genreService;

    public final GenreMapper genreMapper;

    @Transactional
    public GameGetDto createGame(GamePostDto gamePostDto) {

        List<Genre> updateGenreList = handleFrontendGenreList(gamePostDto.getGenreList());
        gamePostDto.setGenreList(updateGenreList);
        Game game = gameMapper.GamePostDtoToGame(gamePostDto);
        isExist(gamePostDto.getName());

        return gameMapper.GameToGameGetDto(gameRepository.save(game));
    }

    @Transactional
    public List<Genre> handleFrontendGenreList(List<Genre> genreList) {
        Map<Boolean, List<Genre>> checkGenres = genreList.stream().collect(Collectors.partitioningBy(item -> item.getId() == null));
        List<Genre> newGetDto = checkGenres.get(true);
        List<Genre> existGetDto = checkGenres.get(false);
        List<Genre> existGenre = genreService.getAllGenre(existGetDto);
        List<Genre> updatedGenreList = new ArrayList<>(existGenre);

        if (newGetDto.size() > 0) {
            List<Genre> createdGenre = genreService.saveAllGenre(newGetDto);
            updatedGenreList.addAll(createdGenre);
        }

        return updatedGenreList;
    }

    public boolean isExist(String name) {

        Boolean isExist = gameRepository.existsUserByName(name);
        if (Boolean.TRUE.equals(isExist)) {
            throw new ResourceExistException("Game already existed!");
        }
        return isExist;
    }

    public GameGetDto updateGame(GameUpdateDto gameUpdateDto, Long id) {
        List<Genre> updateGenreList = handleFrontendGenreList(gameUpdateDto.getGenreList());
        Game game = findActiveGame(id);

        gameUpdateDto.setCreatedTime(game.getCreatedTime());
        gameUpdateDto.setId(id);
        gameUpdateDto.setIsDeleted(game.getIsDeleted());
        gameUpdateDto.setUpdatedTime(OffsetDateTime.now());
        gameUpdateDto.setGenreList(updateGenreList);

        gameMapper.GameUpdateDtoToGame(gameUpdateDto);

        return gameMapper.GameToGameGetDto(gameRepository.save((gameMapper.GameUpdateDtoToGame(gameUpdateDto))));
    }

    public String deleteGame(Long id) {
        Game game = findActiveGame(id);
        game.setIsDeleted(true);
        gameRepository.save(game);
        return "Delete game successfully";
    }

    public Game findActiveGame(Long id) {
        return gameRepository.findGameByIdAndIsDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("game"));
    }

    public GameGetDto getGame(Long id) {
        Game game = findActiveGame(id);
        return gameMapper.GameToGameGetDto(game);
    }
}
