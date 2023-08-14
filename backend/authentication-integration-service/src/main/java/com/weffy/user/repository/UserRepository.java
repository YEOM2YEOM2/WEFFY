package com.weffy.user.repository;

import com.weffy.user.entity.WeffyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<WeffyUser, Long> {

    Optional<WeffyUser> findByIdentification(String identification);
    Optional<WeffyUser> findByEmail(String email);
}
