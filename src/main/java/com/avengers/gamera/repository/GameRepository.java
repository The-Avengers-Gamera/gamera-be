package com.avengers.gamera.repository;

import com.avengers.gamera.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Boolean existsUserByName(String name);

    Optional<Game> findGameByIdAndIsDeletedFalse(Long id);

}
