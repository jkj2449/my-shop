package com.shop.front.controller;

import com.shop.front.dto.cart.CartDeleteRequestsDto;
import com.shop.front.dto.cart.CartListResponseDto;
import com.shop.front.dto.cart.CartSaveRequestDto;
import com.shop.front.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CartController {

    private final CartService cartService;

    @GetMapping("/api/v1/cart/{memberId}")
    public Page<CartListResponseDto> find(@PageableDefault Pageable pageable, @PathVariable("memberId") final Long memberId) {
        return cartService.search(memberId, pageable);
    }

    @PostMapping("/api/v1/cart")
    public Long save(@RequestBody final CartSaveRequestDto requestsDto) {
        return cartService.save(requestsDto);
    }

    @DeleteMapping("/api/v1/cart")
    public void delete(@RequestBody final CartDeleteRequestsDto requestsDto) {
        cartService.deleteAll(requestsDto);
    }
}
