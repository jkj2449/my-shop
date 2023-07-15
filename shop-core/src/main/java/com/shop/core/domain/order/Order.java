package com.shop.core.domain.order;

import com.shop.core.domain.BaseTimeEntity;
import com.shop.core.domain.payment.PayType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@RequiredArgsConstructor
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long memberId;
    @Column(length = 500, nullable = false)
    private String address;
    @Column(length = 50, nullable = false)
    private String cardNumber;
    @Column(length = 2, nullable = false)
    private String bank;
    @Column(nullable = false)
    private Long price;
    @Column(length = 2, nullable = false)
    private OrderStatus orderStatus;
    @Column(length = 2, nullable = false)
    private PayType payType;

    @BatchSize(size = 100)
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "orderId")
    private List<OrderDetail> orderDetail = new ArrayList<>();

    @Builder
    public Order(Long memberId, String address, String cardNumber, String bank, Long price, OrderStatus orderStatus, PayType payType, List<OrderDetail> orderDetail) {
        this.memberId = memberId;
        this.address = address;
        this.cardNumber = cardNumber;
        this.bank = bank;
        this.price = price;
        this.orderStatus = orderStatus;
        this.payType = payType;
        this.orderDetail = orderDetail;
    }
}
