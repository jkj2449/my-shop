package com.shop.front.dto.review;

import com.shop.core.domain.review.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReviewSaveRequestDto {
    private String title;
    private String content;

    @Builder
    public ReviewSaveRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Review toEntity() {
        return Review.builder()
                .title(title)
                .content(content)
                .build();
    }
}