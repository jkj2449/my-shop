package com.shop.core.domain.order;

import com.shop.core.domain.BaseTimeEntity;
import com.shop.core.domain.code.BankCode;
import com.shop.core.domain.code.CardCode;
import com.shop.core.domain.code.OrderStatusCode;
import com.shop.core.domain.code.PayTypeCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity(name = "order")
@NoArgsConstructor
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long memberId;
    @Column(length = 500, nullable = false)
    private String address;
    @Column(length = 50)
    private String cardNumber;
    @Column(length = 2)
    private CardCode cardCode;
    @Column(length = 2)
    private BankCode bankCode;
    @Column(nullable = false)
    private Long price;
    @Setter
    @Column(length = 2, nullable = false)
    private OrderStatusCode orderStatusCode;
    @Column(length = 2, nullable = false)
    private PayTypeCode payTypeCode;

    @BatchSize(size = 100)
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "orderId")
    private List<OrderDetail> orderDetail = new ArrayList<>();

    @Builder
    public Order(Long id, Long memberId, String address, String cardNumber, CardCode cardCode, BankCode bankCode, Long price, OrderStatusCode orderStatusCode, PayTypeCode payTypeCode, List<OrderDetail> orderDetail) {
        this.id = id;
        this.memberId = memberId;
        this.address = address;
        this.cardNumber = cardNumber;
        this.cardCode = cardCode;
        this.bankCode = bankCode;
        this.price = price;
        this.orderStatusCode = orderStatusCode;
        this.payTypeCode = payTypeCode;
        this.orderDetail = orderDetail;
    }
}
