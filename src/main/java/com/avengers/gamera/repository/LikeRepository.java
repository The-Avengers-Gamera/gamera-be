package com.avengers.gamera.repository;

import com.avengers.gamera.entity.LikeKey;
import com.avengers.gamera.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeKey> {

    Optional<Like> findByUserIdAndArticleId(Long userId, Long ArticleId);
}
