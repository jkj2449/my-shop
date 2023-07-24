package com.shop.front.service;

import com.shop.core.domain.cart.Cart;
import com.shop.core.domain.cart.CartRepository;
import com.shop.core.domain.item.Item;
import com.shop.core.domain.item.ItemRepository;
import com.shop.front.dto.cart.CartDeleteRequestsDto;
import com.shop.front.dto.cart.CartListResponseDto;
import com.shop.front.dto.cart.CartSaveRequestDto;
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

    public Page<CartListResponseDto> search(final Long memberId, final Pageable pageable) {
        Page<Cart> page = cartRepository.search(memberId, pageable);

        return page.map(CartListResponseDto::new);
    }

    @Transactional
    public Long save(final CartSaveRequestDto dto) {
        Item item = itemRepository.getReferenceById(dto.getItemId());
        return cartRepository.save(dto.toEntity(item)).getId();
    }

    @Transactional
    public void deleteAll(final CartDeleteRequestsDto requestsDto) {
        cartRepository.deleteByIdIn(requestsDto.getCartIdList());
    }
}