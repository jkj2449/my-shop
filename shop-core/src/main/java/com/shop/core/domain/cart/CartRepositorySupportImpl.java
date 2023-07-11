package com.shop.core.domain.cart;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.shop.core.domain.cart.QCart.cart;
import static com.shop.core.domain.item.QItem.item;

@Repository
public class CartRepositorySupportImpl extends QuerydslRepositorySupport implements CartRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CartRepositorySupportImpl(JPAQueryFactory queryFactory) {
        super(Cart.class);
        this.queryFactory = queryFactory;
    }

    public Cart getCart(final Long id) {
        return queryFactory.selectFrom(cart)
                .where(cart.id.eq(id))
                .fetchOne();
    }

    public Page<Cart> search(final Long memberId, final Pageable pageable) {
        List<Cart> list = queryFactory.select(Projections.constructor(Cart.class, cart.id, cart.memberId, item))
                .from(cart)
                .join(cart.item, item)
                .where(cart.memberId.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(Wildcard.count)
                .from(cart)
                .join(item).on(cart.item.id.eq(item.id))
                .where(cart.memberId.eq(memberId))
                .fetchOne();

        PageImpl<Cart> cart = new PageImpl<>(list, pageable, count);
        return cart;
    }
}
