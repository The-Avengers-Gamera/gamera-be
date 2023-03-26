package com.avengers.gamera.repository;

import com.avengers.gamera.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findTagByIdAndIsDeletedFalse(Long id);

    @Query("update Tag t set t.isDeleted = true where t.id =:id and t.isDeleted = false")
    int deleteTagById(Long id);
}
