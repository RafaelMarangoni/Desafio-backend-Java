package com.challenge.backend.repository;

import com.challenge.backend.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    @Query("UPDATE OrderEntity p SET p.status = :newStatus WHERE p.id = :orderId")
    void updateProductPrice(@Param("orderId") UUID orderId, @Param("newStatus") String newStatus);
}

