package com.avengers.gamera.repository;

import com.avengers.gamera.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Transactional
    @Modifying
    @Query("update Tag t set t.isDeleted = true where t.id = ?1 and t.isDeleted = false")
    int deleteTagById(Long id);
}
