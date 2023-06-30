package com.challenge.backend.service;

import com.challenge.backend.OrderRepository;
import com.challenge.backend.client.RequestsClient;
import com.challenge.backend.dto.Items;
import com.challenge.backend.dto.ProductDTO;
import com.challenge.backend.dto.UserDTO;
import com.challenge.backend.entity.OrderEntity;
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
    private RequestsClient requestsClient;

    @Autowired
    private Utils utils;

    public ResponseCreateOrder createOrder(RequestOrder requestOrder) {
        OrderEntity orderEntity = new OrderEntity();
        Items item = new Items();
        List<Items> itemsList = new ArrayList<>();
        var responseUser = requestsClient.getAll();
        var responseProducts = requestsClient.getProduct();

        var listOfvalidateUser = utils.validadeUser(requestOrder, responseUser);
        List<Integer> productIds = utils.getProductIds(requestOrder);
        List<ProductDTO> matchingProducts = utils.validateProduts(responseProducts, productIds);
        BigDecimal totalPrice = sumPrices(matchingProducts);
        buildItems(item, itemsList, matchingProducts);
        saveOrder(orderEntity, listOfvalidateUser, totalPrice);

        return createOrderResponseBuild(orderEntity, itemsList);

    }

    private static ResponseCreateOrder createOrderResponseBuild(OrderEntity orderEntity, List<Items> itemsList) {
        return ResponseCreateOrder.builder()
                .id(UUID.randomUUID())
                .totalPrice(orderEntity.getTotalPrice())
                .Status("PENDING")
                .items(itemsList)
                .build();
    }

    private void saveOrder(OrderEntity orderEntity, Optional<UserDTO> listOfvalidateUser, BigDecimal totalPrice) {
        orderEntity.setStatus("PENDING");
        orderEntity.setTotalPrice(totalPrice);
        orderEntity.setUserId(listOfvalidateUser.map(UserDTO::getId).orElse(1));
        orderRepository.save(orderEntity);
    }

    private static void buildItems(Items item, List<Items> itemsList, List<ProductDTO> matchingProducts) {
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

            item.setId(productId);
            item.setPrice(product.getPrice());
            item.setAmount(productCounts.get(productId)); // Define a quantidade com base no mapa

            // Calcular o valor parcial (partialAmount) com base no preço e quantidade
            BigDecimal partialAmount = product.getPrice().multiply(BigDecimal.valueOf(item.getAmount()));
            item.setPartialAmount(partialAmount);

            itemsList.add(item);
        }
    }

    private static BigDecimal sumPrices(List<ProductDTO> matchingProducts) {
        return matchingProducts.stream()
                .map(ProductDTO::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}


