package com.shop.front.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.front.config.EmbeddedRedisConfig;
import com.shop.front.config.WithCustomUser;
import com.shop.front.dto.cart.CartSaveRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(EmbeddedRedisConfig.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
class CartControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @WithCustomUser("test1")
    @Test
    public void 장바구니에_담긴다() throws Exception {
        Long memberId = 1L;
        Long itemId = 1L;

        CartSaveRequestDto requestsDto = CartSaveRequestDto.builder()
                .memberId(memberId)
                .itemId(itemId)
                .build();

        String url = "/api/v1/cart";

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestsDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @WithCustomUser("test1")
    @Test
    public void 장바구니_조회된다() throws Exception {
        Long memberId = 1L;
        Long itemId = 1L;

        CartSaveRequestDto requestsDto = CartSaveRequestDto.builder()
                .memberId(memberId)
                .itemId(itemId)
                .build();

        String url = "/api/v1/cart";

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestsDto)))
                .andExpect(status().isOk())
                .andDo(print());


        mockMvc.perform(get(url + "/" + memberId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].item", notNullValue()))
                .andDo(print());
    }
}