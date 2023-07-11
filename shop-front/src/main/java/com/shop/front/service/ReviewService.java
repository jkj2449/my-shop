package com.shop.front.service;

import com.shop.core.domain.review.Review;
import com.shop.core.domain.review.ReviewRepository;
import com.shop.front.common.SecurityContextProvider;
import com.shop.front.dto.review.ReviewResponseDto;
import com.shop.front.dto.review.ReviewUpdateRequestDto;
import com.shop.front.dto.review.ReviewListResponseDto;
import com.shop.front.dto.review.ReviewSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Transactional
    public Long save(ReviewSaveRequestDto requestDto) {
        return reviewRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, ReviewUpdateRequestDto requestDto) {
        Review post = reviewRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 상품평 없습니다. id=" + id));

        if (SecurityContextProvider.getMember().getEmail().equals(post.getCreatedBy())) {
            throw new IllegalArgumentException("해당 상품평 수정 권한이 없습니다.");
        }

        post.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public ReviewResponseDto findById(Long id) {
        Review entity = reviewRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 상품평 없습니다. id=" + id));

        return new ReviewResponseDto(entity);
    }

    public Page<ReviewListResponseDto> findAll(Pageable pageable) {
        return reviewRepository.findAll(pageable).map(ReviewListResponseDto::new);
    }

    @Transactional
    public void delete(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 상품평 없습니다. id=" + id));

        if (SecurityContextProvider.getMember().getEmail().equals(review.getCreatedBy())) {
            throw new IllegalArgumentException("해당 상품평 삭제 권한이 없습니다.");
        }

        reviewRepository.delete(review);
    }
}
