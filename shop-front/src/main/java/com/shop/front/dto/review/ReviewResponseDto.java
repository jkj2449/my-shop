package com.shop.front.dto.review;

import com.shop.core.domain.review.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;

    @Builder
    public ReviewResponseDto(Review entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getCreatedBy();
    }
}
