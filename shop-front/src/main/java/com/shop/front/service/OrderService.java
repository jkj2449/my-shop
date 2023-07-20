package com.shop.front.service;

import com.shop.core.domain.cart.CartRepository;
import com.shop.core.domain.item.Item;
import com.shop.core.domain.item.ItemRepository;
import com.shop.core.domain.order.Order;
import com.shop.core.domain.order.OrderRepository;
import com.shop.front.common.security.SecurityContextProvider;
import com.shop.front.dto.order.OrderListResponseDto;
import com.shop.front.dto.order.OrderSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    @Transactional
    public Long order(final OrderSaveRequestDto requestDto) {
        if (!SecurityContextProvider.getMember().getId().equals(requestDto.getMemberId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        List<Item> items = itemRepository.findByIdIn(requestDto.getItemIdList());

        if (!requestDto.isValidPrice(items)) {
            throw new IllegalArgumentException("주문금액이 올바르지 않습니다.");
        }

        cartRepository.deleteByIdIn(requestDto.getCartIdList());

        return orderRepository.save(requestDto.toEntity()).getId();
    }

    public Page<OrderListResponseDto> search(final Long memberId, final Pageable pageable) {
        if (!SecurityContextProvider.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        Page<Order> search = orderRepository.search(memberId, pageable);
        return search.map(OrderListResponseDto::of);
    }
}
