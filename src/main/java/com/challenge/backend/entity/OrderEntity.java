package com.challenge.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders", schema = "public")
public class OrderEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name= "userid")
    private int userId;

    @Column(name="status")
    private String status;

    @Column(name="totalprice")
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemsEntity> items;

}
