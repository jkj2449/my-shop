package com.shop.core.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
@ExtendWith(SpringExtension.class)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원 테이블에 저장 된다.")
    public void save() {
        Member member = Member.builder()
                .email("test@test.co.kr")
                .username("test")
                .password("1234")
                .role(Role.USER.getRole())
                .build();

        Member save = memberRepository.save(member);

        assertThat(save.getEmail()).isEqualTo(member.getEmail());
        assertThat(save.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    @DisplayName("동일한 이메일로 저장 되지 못한다.")
    public void notDuplicate() {
        Member member = Member.builder()
                .email("test@test.co.kr")
                .username("test")
                .password("1234")
                .role(Role.USER.getRole())
                .build();

        memberRepository.save(member);

        Member member2 = Member.builder()
                .email("test@test.co.kr")
                .username("test")
                .password("1234")
                .role(Role.USER.getRole())
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            memberRepository.save(member2);
        });
    }
}