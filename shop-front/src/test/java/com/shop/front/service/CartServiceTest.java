package com.shop.front.service;

import com.shop.core.domain.cart.Cart;
import com.shop.core.domain.cart.CartRepository;
import com.shop.core.domain.item.Item;
import com.shop.core.domain.member.Role;
import com.shop.front.common.security.MemberDetails;
import com.shop.front.dto.cart.CartListResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @InjectMocks
    CartService cartService;
    @Mock
    CartRepository cartRepository;

    @Test
    @DisplayName("장바구니가 조회된다.")
    public void cart_search_success() {
        MemberDetails member = createMember(1L);
        Pageable pageable = PageRequest.of(1, 1);
        Cart cart = createCart(member.getId());

        given(cartRepository.search(member.getId(), pageable)).willReturn(new PageImpl<>(List.of(cart)));

        Page<CartListResponseDto> search = cartService.search(member.getId(), pageable);

        assertThat(search.getContent().get(0).getId()).isEqualTo(cart.getId());
    }

    private Cart createCart(Long memberId) {
        return Cart.builder()
                .id(1L)
                .memberId(memberId)
                .item(new Item())
                .build();
    }

    private MemberDetails createMember(Long memberId) {
        return MemberDetails.builder()
                .id(memberId)
                .role(Role.USER.getRole())
                .build();
    }
}