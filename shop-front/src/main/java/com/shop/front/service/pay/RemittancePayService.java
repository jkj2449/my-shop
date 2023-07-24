package com.shop.front.service.pay;

import com.shop.core.domain.code.PayTypeCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RemittancePayService implements PayService {
    @Override
    public PayTypeCode type() {
        return PayTypeCode.REMITTANCE;
    }

    @Override
    public void pay() {

    }
}
