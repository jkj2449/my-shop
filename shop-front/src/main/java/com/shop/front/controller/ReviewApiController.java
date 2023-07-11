package com.shop.front.controller;

import com.shop.core.domain.member.Role;
import com.shop.front.dto.review.ReviewUpdateRequestDto;
import com.shop.front.dto.review.ReviewListResponseDto;
import com.shop.front.dto.review.ReviewResponseDto;
import com.shop.front.dto.review.ReviewSaveRequestDto;
import com.shop.front.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ReviewApiController {
    private final ReviewService reviewService;

    @Secured(Role.RoleProperties.ROLE_USER)
    @PostMapping("/api/v1/review")
    public Long save(@RequestBody ReviewSaveRequestDto requestDto) {

        return reviewService.save(requestDto);
    }

    @Secured(Role.RoleProperties.ROLE_USER)
    @PutMapping("/api/v1/review/{id}")
    public Long update(@PathVariable Long id, @RequestBody ReviewUpdateRequestDto requestDto) {
        return reviewService.update(id, requestDto);
    }

    @GetMapping("/api/v1/review/{id}")
    public ReviewResponseDto findById(@PathVariable Long id) {
        return reviewService.findById(id);
    }

    @GetMapping("/api/v1/reviews")
    public Page<ReviewListResponseDto> findById(Pageable pageable) {
        return reviewService.findAll(pageable);
    }

    @Secured(Role.RoleProperties.ROLE_USER)
    @DeleteMapping("/api/v1/review/{id}")
    public Long delete(@PathVariable Long id) {
        reviewService.delete(id);
        return id;
    }
}
