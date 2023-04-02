package com.avengers.gamera.mapper;

import com.avengers.gamera.dto.game.GameGetDto;
import com.avengers.gamera.dto.game.GamePostDto;
import com.avengers.gamera.dto.game.GameSlimGetDto;
import com.avengers.gamera.dto.game.GameUpdateDto;
import com.avengers.gamera.entity.Game;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GameMapper {

    GameGetDto GameToGameGetDto(Game game);
    Game GamePostDtoToGame(GamePostDto gamePostDto);

    Game GameUpdateDtoToGame(GameUpdateDto gameUpdateDto);

    GameSlimGetDto GameToGameSlimGetDto(Game game);
}

