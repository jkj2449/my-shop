package com.shop.front.dto.member;

import com.shop.front.common.security.MemberDetails;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberSingInResponseDto {
    private Long id;
    private String email;
    private String username;

    @Builder
    public MemberSingInResponseDto(MemberDetails member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.username = member.getUsername();
    }
}
