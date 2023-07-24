package com.shop.front.service.event;

import lombok.Getter;

@Getter
public class PayEvent {
    private Long outboxId;

    public PayEvent(Long outboxId) {
        this.outboxId = outboxId;
    }

    public static PayEvent of(Long outboxId) {
        return new PayEvent(outboxId);
    }
}
