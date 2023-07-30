package com.weffy.token.Repository;

import com.weffy.token.Entity.RefreshToken;
import com.weffy.user.Entity.WeffyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByWeffyUser(WeffyUser weffyUser);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
