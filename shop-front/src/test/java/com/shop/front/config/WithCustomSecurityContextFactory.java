package com.shop.front.config;

import com.shop.front.common.security.MemberDetails;
import com.shop.front.dto.member.MemberSignUpRequestDto;
import com.shop.front.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@RequiredArgsConstructor
public class WithCustomSecurityContextFactory implements WithSecurityContextFactory<WithCustomUser> {
    private final MemberService memberService;

    @Override
    public SecurityContext createSecurityContext(WithCustomUser annotation) {

        String username = annotation.value();
        String email = username + "@gmail.com";
        String password = "password";

        MemberSignUpRequestDto requestDto = MemberSignUpRequestDto.builder()
                .email(email)
                .username(username)
                .password(password)
                .passwordConfirm(password)
                .build();

        memberService.signUp(requestDto);

        MemberDetails memberDetails = memberService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberDetails, "", memberDetails.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        return context;
    }
}
