package com.shop.front.service;

import com.shop.core.domain.order.Order;
import com.shop.core.domain.order.OrderRepository;
import com.shop.front.common.SecurityContextProvider;
import com.shop.front.dto.order.OrderListResponseDto;
import com.shop.front.dto.order.OrderSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public Long save(OrderSaveRequestDto requestDto) {
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
