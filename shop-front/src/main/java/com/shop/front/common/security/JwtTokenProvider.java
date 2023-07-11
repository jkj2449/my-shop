package com.shop.front.common.security;

import com.shop.front.util.RequestContextProvider;
import com.shop.front.exception.JwtTokenNotValidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public final class JwtTokenProvider {
    private final RedisTemplate<String, String> redisTemplate;

    public Token createValidToken() {
        HttpServletRequest request = RequestContextProvider.getHttpServletRequest();
        Token token = this.resolveToken(request);

        if (token.isValidToken(token.getAccessToken())) {
            return token;
        }

        if (token.isValidToken(token.getRefreshToken()) && this.isExistsRedisRefreshToken(token)) {
            MemberDetails member = token.toMember(token.getRefreshToken(), JwtProperties.REFRESH_TOKEN_KEY);
            return this.createNewToken(member);
        }

        throw new JwtTokenNotValidException("token not valid");
    }

    public Token createNewToken(final MemberDetails member) {
        Token newToken = Token.builder()
                .accessToken(Token.createToken(member, JwtProperties.ACCESS_TOKEN_KEY, JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
                .refreshToken(Token.createToken(member, JwtProperties.REFRESH_TOKEN_KEY, JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME))
                .build();

        this.insertRedisRefreshToken(newToken);
        this.setTokenInHeaderAndCookie(newToken);

        return newToken;
    }

    public void deleteRefreshToken(final String email) {
        this.deleteRefreshTokenCookie();
        String key = String.join(":", List.of(JwtProperties.REFRESH_TOKEN_KEY, email));
        redisTemplate.delete(key);
    }

    private void deleteRefreshTokenCookie() {
        HttpServletResponse response = RequestContextProvider.getHttpServletResponse();

        Cookie cookie = new Cookie(JwtProperties.REFRESH_TOKEN_KEY, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        Objects.requireNonNull(response).addCookie(cookie);
    }

    private Token resolveToken(final HttpServletRequest request) {
        Cookie refreshTokenCookie = this.getRefreshTokenCookie(request);
        String accessToken = request.getHeader(JwtProperties.HEADER_AUTHORIZATION_KEY);
        String refreshToken = refreshTokenCookie != null ? refreshTokenCookie.getValue() : null;

        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private Cookie getRefreshTokenCookie(final HttpServletRequest request) {
        List<Cookie> cookies = request.getCookies() != null ? List.of(request.getCookies()) : Collections.emptyList();
        return cookies.stream()
                .filter(v -> v.getName().equals(JwtProperties.REFRESH_TOKEN_KEY))
                .findFirst()
                .orElse(null);
    }

    private void setTokenInHeaderAndCookie(final Token token) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

        Cookie cookie = new Cookie(JwtProperties.REFRESH_TOKEN_KEY, token.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(Long.valueOf(Duration.ofMillis(JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME).toSeconds()).intValue());
        cookie.setPath("/");

        Objects.requireNonNull(response).addCookie(cookie);
        response.addHeader(JwtProperties.HEADER_AUTHORIZATION_KEY, token.getAccessToken());
    }

    private boolean isExistsRedisRefreshToken(final Token token) {
        MemberDetails member = token.toMember(token.getRefreshToken(), JwtProperties.REFRESH_TOKEN_KEY);
        String key = String.join(":", JwtProperties.REFRESH_TOKEN_KEY, member.getEmail());
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String refreshToken = valueOperations.get(key);

        return refreshToken != null && refreshToken.equals(token.getRefreshToken());
    }

    private void insertRedisRefreshToken(final Token token) {
        MemberDetails member = token.toMember(token.getRefreshToken(), JwtProperties.REFRESH_TOKEN_KEY);
        String key = String.join(":", JwtProperties.REFRESH_TOKEN_KEY, member.getEmail());

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, token.getRefreshToken(), JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME, TimeUnit.MILLISECONDS);
    }

}
