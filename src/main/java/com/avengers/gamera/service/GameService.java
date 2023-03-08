package com.avengers.gamera.service;

import com.avengers.gamera.dto.game.GameGenrePostDto;
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
        isExist(gamePostDto.getName());

        List<Genre> updateGenreList = handleFrontendGenreList(gamePostDto.getGameGenrePostDtoList());
        Game game = gameMapper.GamePostDtoToGame(gamePostDto);
        game.setGenreList(updateGenreList);

        return gameMapper.GameToGameGetDto(gameRepository.save(game));
    }

    @Transactional
    public List<Genre> handleFrontendGenreList(List<GameGenrePostDto> genreList) {
        Map<Boolean, List<GameGenrePostDto>> checkGenres = genreList.stream().collect(Collectors.partitioningBy(item -> item.getId() == null));
        List<GameGenrePostDto> newGetDto = checkGenres.get(true);
        List<GameGenrePostDto> existGetDto = checkGenres.get(false);
        List<Genre> existGenre = genreService.getAllGenre(existGetDto);
        List<Genre> updatedGenreList = new ArrayList<>(existGenre);

        if (newGetDto.size() > 0) {
            List<Genre> createdGenre = genreService.saveAllGenre(newGetDto);
            updatedGenreList.addAll(createdGenre);
        }

        return updatedGenreList;
    }

    public void isExist(String name) {

        boolean isExist = gameRepository.existsUserByName(name);
        if (isExist) {
            throw new ResourceExistException("Game already existed!");
        }
    }

    @Transactional
    public GameGetDto updateGame(GameUpdateDto gameUpdateDto, Long id) {
        List<Genre> updateGenreList = handleFrontendGenreList(gameUpdateDto.getGameGenrePostDtoList());
        Game game = findActiveGame(id);

        gameUpdateDto.setCreatedTime(game.getCreatedTime());
        gameUpdateDto.setId(id);
        gameUpdateDto.setIsDeleted(game.getIsDeleted());
        gameUpdateDto.setUpdatedTime(OffsetDateTime.now());
        Game updateGame=gameMapper.GameUpdateDtoToGame(gameUpdateDto);
        updateGame.setGenreList(updateGenreList);

        return gameMapper.GameToGameGetDto(gameRepository.save(updateGame));
    }

    public String deleteGame(Long id) {
      int result=  gameRepository.updateIsDeleted(id);
      System.out.println(result);
        return (result == 1L ? "Delete successfully" : "Game not exist");
    }

    public Game findActiveGame(Long id) {
        return gameRepository.findGameByIdAndIsDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("game"));
    }

    public GameGetDto getGame(Long id) {
        Game game = findActiveGame(id);
        return gameMapper.GameToGameGetDto(game);
    }
}
