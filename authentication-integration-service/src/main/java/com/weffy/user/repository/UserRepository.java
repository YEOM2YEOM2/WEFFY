package com.weffy.user.repository;

import com.weffy.user.entity.WeffyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<WeffyUser, Long> {

    Optional<WeffyUser> findByIdentification(String identification);
    Optional<WeffyUser> findByEmail(String email);
}
