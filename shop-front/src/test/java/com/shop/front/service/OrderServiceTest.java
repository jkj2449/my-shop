package com.shop.front.service;

import com.shop.core.domain.cart.CartRepository;
import com.shop.core.domain.code.BankCode;
import com.shop.core.domain.code.CardCode;
import com.shop.core.domain.code.PayTypeCode;
import com.shop.core.domain.item.Item;
import com.shop.core.domain.item.ItemRepository;
import com.shop.core.domain.member.Role;
import com.shop.core.domain.order.Order;
import com.shop.core.domain.order.OrderRepository;
import com.shop.front.common.security.MemberDetails;
import com.shop.front.common.security.SecurityContextProvider;
import com.shop.front.dto.order.OrderDetailSaveRequestDto;
import com.shop.front.dto.order.OrderListResponseDto;
import com.shop.front.dto.order.OrderSaveRequestDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @InjectMocks
    OrderService orderService;
    @Mock
    OrderRepository orderRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    CartRepository cartRepository;
    MemberDetails member;

    @BeforeAll
    public static void setupAll() {
        mockStatic(SecurityContextProvider.class);
    }

    @BeforeEach
    public void setup() {
        this.member = createMember(1L);
        given(SecurityContextProvider.getMember()).willReturn(member);
    }

    @Test
    @DisplayName("주문이 조회된다.")
    public void order_search_success() {
        Pageable pageable = PageRequest.of(1, 1);
        Order order = createOrder(member.getId());

        given(orderRepository.search(member.getId(), pageable)).willReturn(new PageImpl<>(List.of(order)));

        Page<OrderListResponseDto> search = orderService.search(member.getId(), pageable);

        assertThat(search.getContent().get(0).getId()).isEqualTo(order.getId());
    }

    @Test
    @DisplayName("주문이 된다.")
    public void order_success() {
        OrderSaveRequestDto requestDto = this.createOrderSaveRequestDto(PayTypeCode.CARD, CardCode.K_CARD, null);
        Order order = Order.builder().id(1L).build();

        given(itemRepository.findByIdIn(requestDto.getItemIdList())).willReturn(this.createItemList());
        doNothing().when(cartRepository).deleteByIdIn(requestDto.getCartIdList());
        given(orderRepository.save(any(Order.class))).willReturn(order);

        Long orderId = orderService.order(requestDto);

        assertThat(orderId).isEqualTo(order.getId());
    }

    @Test
    @DisplayName("주문 가격이 맞지 않아 실패된다.")
    public void order_fail_price() {
        OrderSaveRequestDto requestDto = this.createOrderSaveRequestDto(PayTypeCode.CARD, CardCode.K_CARD, null);
        requestDto.getOrderDetailList().get(0).setPrice(10000L);

        given(itemRepository.findByIdIn(requestDto.getItemIdList())).willReturn(this.createItemList());

        assertThrows(IllegalArgumentException.class, () -> {
            orderService.order(requestDto);
        });
    }

    @Test
    @DisplayName("주문 상품 갯수가 맞지 않아 실패된다.")
    public void order_fail_item_count() {
        OrderSaveRequestDto requestDto = this.createOrderSaveRequestDto(PayTypeCode.CARD, CardCode.K_CARD, null);

        OrderDetailSaveRequestDto detailRequestDto = OrderDetailSaveRequestDto.builder()
                .cartId(1L)
                .itemId(1L)
                .price(100l)
                .build();

        requestDto.getOrderDetailList().add(detailRequestDto);

        given(itemRepository.findByIdIn(requestDto.getItemIdList())).willReturn(this.createItemList());

        assertThrows(IllegalArgumentException.class, () -> {
            orderService.order(requestDto);
        });
    }

    private List<Item> createItemList() {
        return LongStream.range(1, 5)
                .mapToObj(i -> {
                    return Item.builder()
                            .id(i)
                            .price(i * 100)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private OrderSaveRequestDto createOrderSaveRequestDto(PayTypeCode payTypeCode, CardCode cardCode, BankCode bankCode) {
        return OrderSaveRequestDto.builder()
                .memberId(member.getId())
                .payTypeCode(payTypeCode)
                .cardCode(cardCode)
                .bankCode(bankCode)
                .orderDetailList(this.createOrderDetailSaveRequestDtoList())
                .build();
    }

    private List<OrderDetailSaveRequestDto> createOrderDetailSaveRequestDtoList() {
        List<Item> itemList = this.createItemList();

        return itemList.stream()
                .map(item -> OrderDetailSaveRequestDto.builder()
                        .cartId(item.getId())
                        .itemId(item.getId())
                        .price(item.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    private Order createOrder(Long memberId) {
        return Order.builder()
                .id(1L)
                .memberId(memberId)
                .build();
    }

    private MemberDetails createMember(Long memberId) {
        return MemberDetails.builder()
                .id(memberId)
                .role(Role.USER.getRole())
                .build();
    }
}