package com.shop.core.domain.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositorySupport {
    Page<Order> search(final Long memberId, final Pageable pageable);
}
