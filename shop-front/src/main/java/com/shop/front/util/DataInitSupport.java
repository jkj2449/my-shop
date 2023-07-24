package com.shop.front.util;

import com.shop.core.domain.code.BankCode;
import com.shop.core.domain.code.OrderStatusCode;
import com.shop.core.domain.code.PayTypeCode;
import com.shop.core.domain.item.Item;
import com.shop.core.domain.order.Order;
import com.shop.core.domain.order.OrderDetail;
import com.shop.core.domain.order.OrderRepository;
import com.shop.front.dto.item.ItemSaveRequestDto;
import com.shop.front.dto.member.MemberSignUpRequestDto;
import com.shop.front.service.ItemService;
import com.shop.front.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DataInitSupport implements ApplicationRunner {
    private final ItemService itemService;
    private final MemberService memberService;
    private final OrderRepository orderRepository;

    @Override
    public void run(ApplicationArguments args) {
//        initMember();
//        initItem();
//        initOrder();
    }

    private void initMember() {
        MemberSignUpRequestDto dto = MemberSignUpRequestDto
                .builder()
                .email("test@test.co.kr")
                .username("test")
                .password("1234")
                .passwordConfirm("1234")
                .build();

        memberService.signUp(dto);
    }

    private void initOrder() {
        List<OrderDetail> orderDetailList = IntStream.range(1, 3)
                .mapToObj(i -> OrderDetail.builder()
                        .price(1000L * i)
                        .item(Item.builder().id(Long.valueOf(i)).build())
                        .build())
                .collect(Collectors.toList());

        Order order = Order.builder()
                .memberId(1L)
                .address("test")
                .cardNumber("1234")
                .bankCode(BankCode.SHINHAN_BANK)
                .price(3000L)
                .orderStatusCode(OrderStatusCode.COMPLETED)
                .payTypeCode(PayTypeCode.CARD)
                .orderDetail(orderDetailList)
                .build();

        orderRepository.save(order);
    }

    private void initItem() {
        List<String> imgUrlList = Arrays.asList(
                "/img/bread-4170338_640.jpg",
                "/img/ring-2405145_640.jpg",
                "/img/stainless-878337_640.jpg"
        );

        IntStream.rangeClosed(10, 50)
                .forEach(i -> {
                    Collections.shuffle(imgUrlList);

                    ItemSaveRequestDto dto = ItemSaveRequestDto.builder()
                            .name("상품" + i)
                            .price((long) (i * 100))
                            .description("상품 설명" + i)
                            .imagePath(imgUrlList.get(0))
                            .build();

                    itemService.save(dto);
                });
    }
}
