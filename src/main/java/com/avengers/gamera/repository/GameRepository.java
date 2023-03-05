package com.avengers.gamera.repository;

import com.avengers.gamera.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    boolean existsUserByName(String name);

    @Transactional
    @Modifying
    @Query("update Game g set g.isDeleted=true where g.id=?1 and g.isDeleted=false ")
    int updateIsDeleted (Long id);

    Optional<Game> findGameByIdAndIsDeletedFalse(Long id);

}
