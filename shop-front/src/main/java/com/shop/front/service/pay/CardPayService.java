package com.shop.front.service.pay;

import com.shop.core.domain.code.PayTypeCode;
import com.shop.front.exception.ExternalApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CardPayService implements PayService {
    @Override
    public PayTypeCode type() {
        return PayTypeCode.CARD;
    }

    @Override
    public void pay() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new ExternalApiException(e);
        }
    }
}
