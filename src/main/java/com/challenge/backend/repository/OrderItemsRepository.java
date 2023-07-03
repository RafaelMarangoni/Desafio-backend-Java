package com.challenge.backend.repository;

import com.challenge.backend.entity.OrderItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItemsEntity,Integer> {
}
