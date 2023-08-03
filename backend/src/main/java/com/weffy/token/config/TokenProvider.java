package com.weffy.token.config;

import com.weffy.user.entity.WeffyUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public String generateToken(WeffyUser weffyUser, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date((now.getTime() + expiredAt.toMillis())), weffyUser);
    }

    // 토큰 생성
    private String makeToken(Date expiy, WeffyUser weffyUser) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)   /* JWT의 헤더에 "typ" (토큰의 타입을 나타내는 필드)을 설정 */
                .setIssuer(jwtProperties.getIssuer())   /* issuer(이 토큰을 생성한 서비스)를 설정 */
                .setIssuedAt(now)   /* 토큰이 발행된 시간을 설정 */
                .setExpiration(expiy)   /*  토큰의 만료 시간을 설정 */
                .setSubject(weffyUser.getEmail())   /* 토큰의 subject(토큰이 대상으로 하는 주체)를 설정 */
                .claim("identification", weffyUser.getIdentification())   /*  JWT에 커스텀 클레임을 추가 */
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())   /* HS256 알고리즘과 제공된 시크릿 키를 사용하여 JWT에 서명(서명은 JWT의 무결성을 보장하는데 사용) */
                .compact(); /* JWT를 생성하고, 생성된 JWT를 문자열로 직렬화 */
    }

    // 토큰 유효성 검증
    public boolean validToken(String token) {
        try{
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) /* 비밀값으로 복호화 */
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰 기반으로 인증 정보 get
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new User(claims.getSubject(), "", authorities), token, authorities);
    }

    // 클레임 조회
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("identification", String.class);
    }
}
