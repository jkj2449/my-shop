package com.shop.front.common.security;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@Getter
public class Token {
    private static final ObjectMapper MAPPER;
    private final String accessToken;
    private final String refreshToken;

    static {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER = mapper;
    }

    @Builder
    public Token(final String accessToken, final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public boolean isValidToken(final String token) {
        if (token == null) {
            return false;
        }

        try {
            Jwts.parser().setSigningKey(JwtProperties.ENCODE_SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException |
                 UnsupportedJwtException |
                 MalformedJwtException |
                 SignatureException |
                 IllegalArgumentException e) {
            log.error("token parse error", e);
            return false;
        }
    }

    public MemberDetails toMember(final String token, final String tokenKey) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(JwtProperties.ENCODE_SECRET_KEY).parseClaimsJws(token);
        return MAPPER.convertValue(claims.getBody().get(tokenKey), MemberDetails.class);
    }

    public static String createToken(final MemberDetails member, final String tokenKey, long expirationTime) {
        MemberClaim memberClaim = MAPPER.convertValue(member, MemberClaim.class);

        long now = System.currentTimeMillis();

        return Jwts.builder()
                .claim(tokenKey, memberClaim) // 정보 저장
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, JwtProperties.ENCODE_SECRET_KEY)  // 사용할 암호화 알고리즘과 signature 에 들어갈 secret값 세팅
                .compact();
    }

    @Getter
    @Setter
    private static class MemberClaim {
        private Long id;
        private String email;
        private String username;
        private String role;
    }

}