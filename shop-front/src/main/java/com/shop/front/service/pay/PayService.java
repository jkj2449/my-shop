package com.shop.front.service.pay;

import com.shop.core.domain.code.PayTypeCode;

public interface PayService {
    PayTypeCode type();

    void pay();
}
