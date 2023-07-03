package com.challenge.backend.service;

import com.challenge.backend.client.RequestsClient;
import com.challenge.backend.dto.Items;
import com.challenge.backend.dto.ProductDTO;
import com.challenge.backend.dto.UserDTO;
import com.challenge.backend.entity.OrderEntity;
import com.challenge.backend.entity.OrderItemsEntity;
import com.challenge.backend.repository.OrderItemsRepository;
import com.challenge.backend.repository.OrderRepository;
import com.challenge.backend.request.RequestOrder;
import com.challenge.backend.response.ResponseCreateOrder;
import com.challenge.backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private RequestsClient requestsClient;

    @Autowired
    private Utils utils;

    public ResponseCreateOrder createOrder(RequestOrder requestOrder) {
        OrderEntity orderEntity = new OrderEntity();
        List<OrderItemsEntity> orderItemsList = new ArrayList<>();

        var responseUser = requestsClient.getAll();
        var responseProducts = requestsClient.getProduct();

        var listOfvalidateUser = utils.validadeUser(requestOrder, responseUser);
        List<Integer> productIds = utils.getProductIds(requestOrder);
        List<ProductDTO> matchingProducts = utils.validateProduts(responseProducts, productIds);

        BigDecimal totalPrice = sumPrices(matchingProducts);
        buildItems(orderItemsList, matchingProducts);
        saveOrder(orderEntity, listOfvalidateUser, totalPrice, orderItemsList);

        return createOrderResponseBuild(orderEntity, orderItemsList);
    }

    private static ResponseCreateOrder createOrderResponseBuild(OrderEntity orderEntity, List<OrderItemsEntity> orderItemsList) {
        List<Items> itemsList = new ArrayList<>();

        for (OrderItemsEntity orderItemsEntity : orderItemsList) {
            Items item = new Items();
            item.setId(orderItemsEntity.getProductId());
            item.setPrice(orderItemsEntity.getPrice());
            item.setAmount(orderItemsEntity.getQuantity());
            item.setPartialAmount(orderItemsEntity.getPrice().multiply(BigDecimal.valueOf(orderItemsEntity.getQuantity())));
            itemsList.add(item);
        }

        return ResponseCreateOrder.builder()
                .id(orderEntity.getId())
                .totalPrice(orderEntity.getTotalPrice())
                .status(orderEntity.getStatus())
                .items(itemsList)
                .build();
    }

    private void saveOrder(OrderEntity orderEntity, Optional<UserDTO> listOfvalidateUser, BigDecimal totalPrice, List<OrderItemsEntity> orderItemsList) {
        orderEntity.setId(UUID.randomUUID());
        orderEntity.setStatus("PENDING");
        orderEntity.setTotalPrice(totalPrice);
        orderEntity.setUserId(listOfvalidateUser.map(UserDTO::getId).orElseThrow());
        orderEntity.setItems(orderItemsList); // Define a lista de itens do pedido
        orderItemsList.forEach(orderItemsEntity -> orderItemsEntity.setOrder(orderEntity)); // Define a relação inversa entre item e pedido
        orderRepository.save(orderEntity);
    }

    private static void buildItems(List<OrderItemsEntity> orderItemsList, List<ProductDTO> matchingProducts) {
        Map<Integer, Integer> productCounts = new HashMap<>();

        for (ProductDTO product : matchingProducts) {
            int productId = product.getId();

            // Verifica se o ID do produto já existe no mapa
            if (productCounts.containsKey(productId)) {
                // Se existir, incrementa a quantidade
                int currentCount = productCounts.get(productId);
                productCounts.put(productId, currentCount + 1);
            } else {
                // Se não existir, adiciona o ID do produto ao mapa com quantidade 1
                productCounts.put(productId, 1);
            }

            OrderItemsEntity orderItemsEntity = new OrderItemsEntity();
            orderItemsEntity.setPrice(product.getPrice());
            orderItemsEntity.setProductId(product.getId());
            orderItemsEntity.setQuantity(productCounts.get(productId));
            orderItemsList.add(orderItemsEntity);
        }
    }

    private static BigDecimal sumPrices(List<ProductDTO> matchingProducts) {
        return matchingProducts.stream()
                .map(ProductDTO::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
