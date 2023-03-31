package com.avengers.gamera.controller;

import com.avengers.gamera.dto.game.GameGetDto;
import com.avengers.gamera.dto.game.GamePostDto;
import com.avengers.gamera.dto.game.GameUpdateDto;
import com.avengers.gamera.service.GameService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("games")
public class GameController {
    public final GameService gameService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameGetDto createGame(@Valid @RequestBody GamePostDto gamePostDto) {
        return gameService.createGame(gamePostDto);
    }

    @GetMapping("/{id}")
    public GameGetDto getGame(@PathVariable Long id) {
        return gameService.getGame(id);
    }

    @PutMapping("/{id}")
    public GameGetDto updateGame(@Valid @RequestBody GameUpdateDto gameUpdateDto, @PathVariable Long id) {
        return gameService.updateGame(gameUpdateDto, id);
    }

    @DeleteMapping("/{id}")
    public String deleteGame(@PathVariable Long id) {
        return gameService.deleteGame(id);
    }

    @GetMapping
    public List<GameGetDto> getGames(){
        return gameService.getAllGames();
    }
}
