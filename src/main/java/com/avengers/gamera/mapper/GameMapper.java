package com.avengers.gamera.mapper;

import com.avengers.gamera.dto.game.GameGetDto;
import com.avengers.gamera.dto.game.GamePostDto;
import com.avengers.gamera.dto.game.GameUpdateDto;
import com.avengers.gamera.entity.Game;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GameMapper {

    GameGetDto GameToGameGetDto(Game game);


    @Mapping(source = "name",target = "name")
    @Mapping(source = "platform",target = "platform")
    @Mapping(source = "releaseDate",target = "releaseDate")
    @Mapping(source = "country",target = "country")
    @Mapping(source = "scores",target = "scores")
    @Mapping(source = "developers",target = "developers")
    @Mapping(source = "publishers",target = "publishers")
    @Mapping(source = "introduction",target = "introduction")
    @Mapping(source = "description",target = "description")
    Game GamePostDtoToGame(GamePostDto gamePostDto);

    Game GameUpdateDtoToGame(GameUpdateDto gameUpdateDto);

}

