package com.itteam.estatesapi.repository;

import com.itteam.estatesapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.isEmailConfirmed = TRUE WHERE u.email = ?1")
    int confirmUserByEmail(String email);
}