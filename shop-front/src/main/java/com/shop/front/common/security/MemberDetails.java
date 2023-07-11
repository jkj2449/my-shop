package com.shop.front.common.security;

import com.shop.core.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@NoArgsConstructor
public class MemberDetails implements UserDetails {
    private Long id;
    private String email;
    private String username;
    private String password;
    private String role;

    @Builder
    public MemberDetails(Long id, String email, String username, String password, String role) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Member toEntity() {
        return Member.builder()
                .id(this.id)
                .email(this.email)
                .username(this.username)
                .password(this.password)
                .role(this.role)
                .build();
    }

    public static MemberDetails of(Member member) {
        return MemberDetails.builder()
                .id(member.getId())
                .email(member.getEmail())
                .username(member.getUsername())
                .password(member.getPassword())
                .role(member.getRole())
                .build();
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
