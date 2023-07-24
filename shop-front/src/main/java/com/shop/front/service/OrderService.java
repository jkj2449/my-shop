package com.shop.front.service;

import com.shop.core.domain.cart.CartRepository;
import com.shop.core.domain.code.OutboxTypeCode;
import com.shop.core.domain.item.Item;
import com.shop.core.domain.item.ItemRepository;
import com.shop.core.domain.order.Order;
import com.shop.core.domain.order.OrderRepository;
import com.shop.core.domain.outbox.OutBox;
import com.shop.core.domain.outbox.OutBoxRepository;
import com.shop.front.dto.order.OrderListResponseDto;
import com.shop.front.dto.order.OrderSaveRequestDto;
import com.shop.front.service.event.PayEvent;
import com.shop.front.util.ObjectMapperProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final ApplicationEventPublisher eventPublisher;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final OutBoxRepository outBoxRepository;

    @Transactional
    public Long order(final OrderSaveRequestDto requestDto) {
        requestDto.validatePayType();

        List<Item> items = itemRepository.findByIdIn(requestDto.getItemIdList());
        requestDto.validatePrice(items);

        if (!CollectionUtils.isEmpty(requestDto.getCartIdList())) {
            cartRepository.deleteByIdIn(requestDto.getCartIdList());
        }

        Order order = orderRepository.save(requestDto.toEntity(items));

        OutBox outbox = outBoxRepository.saveAndFlush(
                OutBox.of(OutboxTypeCode.Pay, ObjectMapperProvider.serialization(order))
        );
        eventPublisher.publishEvent(PayEvent.of(outbox.getId()));

        return order.getId();
    }

    public Page<OrderListResponseDto> search(final Long memberId, final Pageable pageable) {
        Page<Order> search = orderRepository.search(memberId, pageable);
        return search.map(OrderListResponseDto::of);
    }
}
