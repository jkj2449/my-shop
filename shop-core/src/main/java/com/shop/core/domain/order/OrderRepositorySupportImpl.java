package com.shop.core.domain.order;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.shop.core.domain.order.QOrder.order;
import static com.shop.core.domain.order.QOrderDetail.orderDetail;


@Repository
public class OrderRepositorySupportImpl extends QuerydslRepositorySupport implements OrderRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public OrderRepositorySupportImpl(JPAQueryFactory queryFactory) {
        super(Order.class);
        this.queryFactory = queryFactory;
    }

    public Page<Order> search(final Long memberId, final Pageable pageable) {
        List<Order> list = queryFactory.select(order)
                .from(order, orderDetail)
                .where(order.memberId.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        Long count = queryFactory.select(Wildcard.count)
                .from(order)
                .join(order.orderDetail, orderDetail)
                .where(order.memberId.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetchOne();

        PageImpl<Order> orders = new PageImpl<>(list, pageable, count);

        return orders;
    }
}
