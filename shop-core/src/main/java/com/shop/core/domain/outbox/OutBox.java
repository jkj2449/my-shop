package com.shop.core.domain.outbox;

import com.shop.core.domain.BaseTimeEntity;
import com.shop.core.domain.code.OutboxStatusCode;
import com.shop.core.domain.code.OutboxTypeCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class OutBox extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Column
    private String failReason;
    @Column(nullable = false)
    private OutboxTypeCode outboxTypeCode;
    @Setter
    @Column(nullable = false)
    private OutboxStatusCode outboxStatusCode;
    @Setter
    @Getter
    @Column(columnDefinition = "TEXT")
    private String payload;

    public OutBox(OutboxTypeCode outboxTypeCode, OutboxStatusCode outboxStatusCode, String payload) {
        this.outboxTypeCode = outboxTypeCode;
        this.outboxStatusCode = outboxStatusCode;
        this.payload = payload;
    }

    public static OutBox of(OutboxTypeCode outboxTypeCode, String payload) {
        return new OutBox(outboxTypeCode, OutboxStatusCode.PENDING, payload);
    }
}
