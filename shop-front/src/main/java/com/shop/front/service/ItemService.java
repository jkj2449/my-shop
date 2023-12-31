package com.shop.front.service;

import com.shop.core.domain.item.Item;
import com.shop.core.domain.item.ItemRepository;
import com.shop.front.dto.item.ItemListResponseDto;
import com.shop.front.dto.item.ItemResponseDto;
import com.shop.front.dto.item.ItemSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemResponseDto findById(final Long id) {
        Item entity = itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 아이템이 없습니다. id=" + id));
        return new ItemResponseDto(entity);
    }

    @Transactional
    public Long save(final ItemSaveRequestDto requestDto) {
        return itemRepository.save(requestDto.toEntity()).getId();
    }

    public Page<ItemListResponseDto> search(final Pageable pageable) {
        return itemRepository.findAll(pageable).map(ItemListResponseDto::new);
    }

    public Long getTotalPriceSum(List<Long> itemIdList) {
        List<Item> items = itemRepository.findByIdIn(itemIdList);
        return items.stream().mapToLong(item -> item.getPrice()).sum();
    }
}
