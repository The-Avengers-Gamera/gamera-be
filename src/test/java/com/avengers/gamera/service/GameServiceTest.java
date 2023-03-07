package com.avengers.gamera.service;

import com.avengers.gamera.dto.game.GameGenrePostDto;
import com.avengers.gamera.dto.game.GameGetDto;
import com.avengers.gamera.dto.game.GamePostDto;
import com.avengers.gamera.dto.game.GameUpdateDto;
import com.avengers.gamera.dto.genre.GenreGetDto;
import com.avengers.gamera.entity.Game;
import com.avengers.gamera.entity.Genre;
import com.avengers.gamera.mapper.GameMapper;
import com.avengers.gamera.repository.GameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {
    @Mock
    private GameRepository gameRepository;
    @Mock
    private GameMapper gameMapper;

    @Mock
    private GenreService genreService;

    @InjectMocks
    private GameService gameService;

    GameGenrePostDto mockGenre2 = GameGenrePostDto.builder().id(1L).name("ZZ").build();
    Genre mockGenre =Genre.builder().id(1L).name("ZZ").createdTime(OffsetDateTime.now()).updatedTime(OffsetDateTime.now()).build();
    Genre mockGenre1 = Genre.builder().id(1L).name("HH").createdTime(OffsetDateTime.now()).updatedTime(OffsetDateTime.now()).build();
  
    Genre mockGenre3 = Genre.builder().name("HH").build();
    int mockResult = 1;

    List<GameGenrePostDto> updatedGenreList = List.of(mockGenre2);
    GenreGetDto genreLst = GenreGetDto.builder().id(1L).name("ZZ").createdTime(OffsetDateTime.now()).updatedTime(OffsetDateTime.now()).build();
    List<GenreGetDto> genreGetDtoList =List.of(genreLst);

    private final GamePostDto gamePostDto = GamePostDto.builder().name("Game1").description("Excellent game").gameGenrePostDtoList(updatedGenreList).build();
    private final Game mockGame = Game.builder()
            .id(1L)
            .name("Game1")
            .description("Excellent game")
            .isDeleted(false)
            .createdTime(OffsetDateTime.now())
            .updatedTime(OffsetDateTime.now())
            .genreList(List.of(mockGenre))
            .build();
    private final Long mockGameId = mockGame.getId();

    private final GameGetDto mockGameGetDto = GameGetDto.builder()
            .id(1L)
            .name("Game1")
            .description("Excellent game")
            .createdTime(OffsetDateTime.now())
            .updatedTime(OffsetDateTime.now())
            .genreList(genreGetDtoList)
            .build();
    private final GameUpdateDto mockGameUpdateDto = GameUpdateDto.builder()
            .name("Game2")
            .description("very good game")
            .isDeleted(false)
            .createdTime(OffsetDateTime.now())
            .updatedTime(OffsetDateTime.now())
            .gameGenrePostDtoList(updatedGenreList)
            .build();

    private final Game mockUpdateGame = Game.builder()
            .name("Game2")
            .description("very good game")
            .isDeleted(false)
            .createdTime(OffsetDateTime.now())
            .updatedTime(OffsetDateTime.now())
            .build();


    @Test
    void shouldSaveNewGameInGameRpoWhenCreateGame() {
        when(genreService.getAllGenre(gamePostDto.getGameGenrePostDtoList())).thenReturn(List.of(mockGenre));
        when(gameMapper.GamePostDtoToGame(gamePostDto)).thenReturn(mockGame);

        gameService.createGame(gamePostDto);
        verify(gameRepository).save(mockGame);
    }

    @Test
    void shouldGetGameGetDtoWhenGetGame() {
        when(gameRepository.findGameByIdAndIsDeletedFalse(mockGameId)).thenReturn(Optional.ofNullable(mockGame));
        when(gameMapper.GameToGameGetDto(mockGame)).thenReturn(mockGameGetDto);

        GameGetDto gameGetDto = gameService.getGame(mockGameId);
        assertEquals(gameGetDto, mockGameGetDto);
    }

    @Test
    void shouldUpdateGameWhenUpdate() {
        Long id = 1L;
        when(gameRepository.findGameByIdAndIsDeletedFalse(id)).thenReturn(Optional.ofNullable(mockGame));
        when(gameMapper.GameUpdateDtoToGame(mockGameUpdateDto)).thenReturn(mockUpdateGame);
        when(gameMapper.GameToGameGetDto(gameRepository.save(mockUpdateGame))).thenReturn(mockGameGetDto);

        GameGetDto gameGetDto = gameService.updateGame(mockGameUpdateDto, id);
        assertEquals(gameGetDto, mockGameGetDto);


    }

    @Test
    void shouldDeleteGame() {
        Long id = 1L;
        when(gameRepository.updateIsDeleted(id)).thenReturn(mockResult);

        String delete = gameService.deleteGame(id);
        assertEquals(delete, "Delete successfully");
    }
}
