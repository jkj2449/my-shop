package com.shop.front.controller;

import com.shop.front.common.security.JwtProperties;
import com.shop.front.common.security.JwtTokenProvider;
import com.shop.front.common.security.MemberDetails;
import com.shop.front.common.security.Token;
import com.shop.front.dto.member.MemberSignInRequestDto;
import com.shop.front.dto.member.MemberSignOutRequestDto;
import com.shop.front.dto.member.MemberSignUpRequestDto;
import com.shop.front.dto.member.MemberSingInResponseDto;
import com.shop.front.exception.JwtTokenNotValidException;
import com.shop.front.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class SignApiController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    // 로그인
    @PostMapping("/api/v1/signIn")
    public MemberSingInResponseDto signIn(@RequestBody final MemberSignInRequestDto requestDto) {
        return memberService.signIn(requestDto);
    }

    @PostMapping("/api/v1/signUp")
    public ResponseEntity<Void> signUp(@RequestBody final MemberSignUpRequestDto requestDto) {
        memberService.signUp(requestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v1/signOut")
    public ResponseEntity<Void> signOut(@RequestBody final MemberSignOutRequestDto requestDto) {
        memberService.signOut(requestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/v1/refresh")
    public ResponseEntity<MemberSingInResponseDto> refresh() {
        try {
            Token token = jwtTokenProvider.createValidToken();
            MemberDetails member = token.toMember(token.getAccessToken(), JwtProperties.ACCESS_TOKEN_KEY);
            return new ResponseEntity<>(new MemberSingInResponseDto(member), HttpStatus.OK);
        } catch (JwtTokenNotValidException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
