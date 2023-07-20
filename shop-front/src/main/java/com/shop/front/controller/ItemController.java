package com.shop.front.controller;

import com.shop.front.dto.item.ItemListResponseDto;
import com.shop.front.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/api/v1/items")
    public Page<ItemListResponseDto> findAll(@PageableDefault Pageable pageable) {
        return itemService.search(pageable);
    }
}
