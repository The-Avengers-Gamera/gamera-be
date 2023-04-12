package com.avengers.gamera.repository;

import com.avengers.gamera.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findUserByIdAndIsDeletedFalse(Long userId);

    Optional<User> findAllByIsDeletedFalse();

    @Modifying
    @Query("update User u set u.isVerified=true where u.id = :id and u.isVerified=false and u.isDeleted=false")
    void updateUserVerify(@Param("id") Long id);
}
