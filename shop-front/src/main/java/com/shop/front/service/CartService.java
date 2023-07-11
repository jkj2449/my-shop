package com.shop.front.service;

import com.shop.core.domain.cart.Cart;
import com.shop.core.domain.cart.CartRepository;
import com.shop.core.domain.item.Item;
import com.shop.core.domain.item.ItemRepository;
import com.shop.front.common.SecurityContextProvider;
import com.shop.front.dto.cart.CartSaveRequestsDto;
import com.shop.front.dto.cart.CartDeleteRequestsDto;
import com.shop.front.dto.cart.CartListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    public Page<CartListResponseDto> findByMemberId(final Long memberId, final Pageable pageable) {
        if (!SecurityContextProvider.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        Page<Cart> search = cartRepository.search(memberId, pageable);

        return search.map(CartListResponseDto::new);
    }

    @Transactional
    public Long save(final CartSaveRequestsDto dto) {
        if (!SecurityContextProvider.getMember().getId().equals(dto.getMemberId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        Item item = itemRepository.getReferenceById(dto.getItemId());

        return cartRepository.save(dto.toEntity(item)).getId();
    }

    @Transactional
    public void deleteAll(final CartDeleteRequestsDto requestsDto) {
        cartRepository.deleteByIdIn(requestsDto.getCartIdList());
    }
}