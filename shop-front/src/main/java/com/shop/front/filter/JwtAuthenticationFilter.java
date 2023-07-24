package com.shop.front.filter;

import com.shop.front.common.security.JwtProperties;
import com.shop.front.common.security.JwtTokenProvider;
import com.shop.front.common.security.MemberDetails;
import com.shop.front.common.security.Token;
import com.shop.front.config.SecurityConfig;
import com.shop.front.exception.JwtTokenNotValidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Token token = jwtTokenProvider.createValidToken();
            this.setAuthentication(token);

        } catch (JwtTokenNotValidException e) {
            log.info("jwt not invalid", e);
        } catch (Exception e) {
            log.error("JwtAuthenticationFilter error", e);
            response.setHeader("error", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return SecurityConfig.excludePath.stream().anyMatch(v -> v.equals(path));
    }

    private void setAuthentication(Token token) {
        MemberDetails member = token.toMember(token.getAccessToken(), JwtProperties.ACCESS_TOKEN_KEY);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member, "", member.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
