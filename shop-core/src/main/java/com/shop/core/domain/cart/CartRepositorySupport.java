package com.shop.core.domain.cart;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CartRepositorySupport {
    Cart getCart(final Long id);

    Page<Cart> search(final Long memberId, final Pageable pageable);
}
