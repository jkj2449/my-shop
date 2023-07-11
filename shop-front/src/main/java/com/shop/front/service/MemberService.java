package com.shop.front.service;


import com.shop.core.domain.member.Member;
import com.shop.core.domain.member.MemberRepository;
import com.shop.core.domain.member.Role;
import com.shop.front.common.security.JwtTokenProvider;
import com.shop.front.common.security.MemberDetails;
import com.shop.front.dto.member.MemberSignInRequestDto;
import com.shop.front.dto.member.MemberSingInResponseDto;
import com.shop.front.dto.member.MemberSignOutRequestDto;
import com.shop.front.dto.member.MemberSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public MemberSingInResponseDto signIn(final MemberSignInRequestDto requestDto) {
        Member entity = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 email이 없습니다."));

        MemberDetails member = MemberDetails.of(entity);

        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        jwtTokenProvider.createNewToken(member);

        return MemberSingInResponseDto.builder()
                .member(member)
                .build();
    }

    @Override
    public MemberDetails loadUserByUsername(final String email) {
        Member entity = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 email이 없습니다."));

        return MemberDetails.of(entity);
    }

    @Transactional
    public void signUp(final MemberSignUpRequestDto requestDto) {
        if (!requestDto.isMatchedPasswordConfirm()) {
            throw new IllegalArgumentException("패스워드 확인이 패스워드와 불일치");
        }

        MemberDetails member = MemberDetails.builder()
                .email(requestDto.getEmail())
                .username(requestDto.getUsername())
                .password(requestDto.getPassword())
                .role(Role.USER.getRole())
                .build();

        member.encodePassword(passwordEncoder);
        memberRepository.save(member.toEntity());
    }

    public void signOut(final MemberSignOutRequestDto requestDto) {
        jwtTokenProvider.deleteRefreshToken(requestDto.getEmail());
    }
}
