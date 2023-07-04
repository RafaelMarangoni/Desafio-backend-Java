package com.challenge.backend.service;

import com.challenge.backend.client.RequestsClient;
import com.challenge.backend.dto.ProductDTO;
import com.challenge.backend.dto.UserDTO;
import com.challenge.backend.entity.OrderEntity;
import com.challenge.backend.entity.OrderItemsEntity;
import com.challenge.backend.repository.OrderItemsRepository;
import com.challenge.backend.repository.OrderRepository;
import com.challenge.backend.request.Id;
import com.challenge.backend.request.RequestOrder;
import com.challenge.backend.response.ResponseCreateOrder;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemsRepository orderItemsRepository;

    @Mock
    private RequestsClient requestsClient;

    @InjectMocks
    private OrderService orderService;


    @Test
    public void testCreateOrder() {
        // Mock dos objetos necessários
        RequestOrder requestOrder = new RequestOrder();
        requestOrder.setUserId(1);
        List<Id> productIds = new ArrayList<>();
        productIds.add(new Id(1));
        productIds.add(new Id(1));
        requestOrder.setProducts(productIds);

        List<ProductDTO> responseProducts = new ArrayList<>();
        responseProducts.add(new ProductDTO(1, "Product 1", new BigDecimal("100.00")));
        responseProducts.add(new ProductDTO(1, "Product 1", new BigDecimal("100.00")));
        when(requestsClient.getProduct()).thenReturn(responseProducts);

        when(orderRepository.save(any())).thenReturn(new OrderEntity());
        when(orderItemsRepository.saveAll(any())).thenReturn(new ArrayList<>());

        // Chamar o método a ser testado
        ResponseCreateOrder response = orderService.createOrder(requestOrder);

        // Verificar os resultados
        assertEquals("PENDING", response.getStatus());
        assertEquals(new BigDecimal("200.00"), response.getTotalPrice());
        assertEquals(1, response.getItems().size());
        assertEquals(1, response.getItems().get(0).getId());
        assertEquals(new BigDecimal("100.00"), response.getItems().get(0).getPrice());
        assertEquals(2, response.getItems().get(0).getAmount());
        assertEquals(new BigDecimal("200.00"), response.getItems().get(0).getPartialAmount());
    }

    @Test
    public void testAddItemToOrder() {

        RequestOrder requestOrder = new RequestOrder();
        requestOrder.setId(UUID.randomUUID());
        requestOrder.setUserId(1);
        List<Id> productIds = new ArrayList<>();
        productIds.add(new Id(1));
        productIds.add(new Id(1));
        requestOrder.setProducts(productIds);

        OrderEntity orderEntity = new OrderEntity();
        List<OrderItemsEntity> orderItemsList = new ArrayList<>();
        Optional<UserDTO> userId = Optional.of(new UserDTO(1, "User 1"));

        List<ProductDTO> responseProducts = new ArrayList<>();
        responseProducts.add(new ProductDTO(1, "Product 1", new BigDecimal("100.00")));
        responseProducts.add(new ProductDTO(1, "Product 1", new BigDecimal("100.00")));
        when(requestsClient.getProduct()).thenReturn(responseProducts);

        when(orderRepository.findById(any())).thenReturn(Optional.of(orderEntity));
        when(orderRepository.save(any())).thenReturn(orderEntity);
        when(orderItemsRepository.saveAll(any())).thenReturn(orderItemsList);


        ResponseCreateOrder response = orderService.addItemToOrder(requestOrder);


        assertEquals("PENDING", response.getStatus());
        assertEquals(BigDecimal.ZERO, response.getTotalPrice());
        assertEquals(1, response.getItems().size());
        assertEquals(1, response.getItems().get(0).getId());
        assertEquals(new BigDecimal("100.00"), response.getItems().get(0).getPrice());
        assertEquals(2, response.getItems().get(0).getAmount());
        assertEquals(BigDecimal.ZERO, response.getItems().get(0).getPartialAmount());
    }
}
