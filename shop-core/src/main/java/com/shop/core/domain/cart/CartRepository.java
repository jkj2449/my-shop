package com.shop.core.domain.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long>, CartRepositorySupport {
    @Modifying(clearAutomatically = true)
    @Query("delete from Cart c where c.id in :idList")
    void deleteByIdIn(@Param("idList") List<Long> idList);

    List<Cart> findByIdIn(List<Long> cartIdList);
}
