package com.weffy.token.repository;

import com.weffy.token.entity.RefreshToken;
import com.weffy.user.entity.WeffyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByWeffyUser(WeffyUser weffyUser);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
