package com.shop.front.service.pay;

import com.shop.core.domain.code.OrderStatusCode;
import com.shop.core.domain.code.OutboxStatusCode;
import com.shop.core.domain.code.PayTypeCode;
import com.shop.core.domain.order.Order;
import com.shop.core.domain.order.OrderRepository;
import com.shop.core.domain.outbox.OutBox;
import com.shop.core.domain.outbox.OutBoxRepository;
import com.shop.front.util.ObjectMapperProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayResolveService {
    private final List<PayService> payServiceList;
    private final OutBoxRepository outBoxRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void pay(final Long outboxId) {
        OutBox outBox = this.findOutBox(outboxId);
        Order order = this.findOrder(outBox);
        PayService payService = this.findPayService(order.getPayTypeCode());

        try {
            payService.pay();
            order.setOrderStatusCode(OrderStatusCode.COMPLETED);
            outBox.setOutboxStatusCode(OutboxStatusCode.SUCCEED);
        } catch (Exception e) {
            this.resolveError(outBox, e);
        }
    }

    private PayService findPayService(PayTypeCode payTypeCode) {
        return this.payServiceList.stream()
                .filter(service -> payTypeCode == service.type())
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("not fount service = " + payTypeCode.getName()));
    }

    private Order findOrder(final OutBox outBox) {
        Order order = ObjectMapperProvider.deserialization(outBox.getPayload(), Order.class);
        return orderRepository.findById(order.getId())
                .orElseThrow(() -> new IllegalArgumentException("not found order = " + order.getId()));
    }

    private OutBox findOutBox(final Long outboxId) {
        return outBoxRepository.findById(outboxId)
                .orElseThrow(() -> new IllegalArgumentException("not found outbox = " + outboxId));
    }

    public void resolveError(OutBox outBox, Exception e) {
        log.error("결제 오류 = " + e.getMessage(), e);
    
        outBox.setOutboxStatusCode(OutboxStatusCode.FAILED);
        outBox.setFailReason(e.getMessage());
        outBoxRepository.save(outBox);
    }
}
