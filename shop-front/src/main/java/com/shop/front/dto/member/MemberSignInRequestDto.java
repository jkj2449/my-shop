package com.shop.front.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberSignInRequestDto {
    private String email;
    private String password;

    @Builder
    public MemberSignInRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
