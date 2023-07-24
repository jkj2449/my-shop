package com.shop.front.service.event;

import com.shop.front.service.pay.PayResolveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PayEventListener {
    private final PayResolveService payResolveService;

    @Async
    @EventListener
    public void pay(PayEvent payEvent) {
        Long outboxId = payEvent.getOutboxId();
        payResolveService.pay(outboxId);
    }
}
