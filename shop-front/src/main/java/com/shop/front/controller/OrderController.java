package com.shop.front.controller;

import com.shop.front.dto.order.OrderListResponseDto;
import com.shop.front.dto.order.OrderSaveRequestDto;
import com.shop.front.service.OrderService;
import com.shop.front.util.SecurityContextProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/api/v1/orders/{memberId}")
    public Page<OrderListResponseDto> search(@PageableDefault Pageable pageable, @PathVariable("memberId") final Long memberId) {
        if (!SecurityContextProvider.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        return orderService.search(memberId, pageable);
    }

    @PostMapping("/api/v1/order")
    public Long save(@RequestBody OrderSaveRequestDto requestDto) {
        if (!SecurityContextProvider.getMember().getId().equals(requestDto.getMemberId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        return orderService.order(requestDto);
    }
}
