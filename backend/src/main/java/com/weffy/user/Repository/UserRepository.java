package com.weffy.user.Repository;

import com.weffy.user.Entity.WeffyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<WeffyUser, Long> {

    Optional<WeffyUser> findByIdentification(String identification);
}
