package com.challenge.backend.service;

import com.challenge.backend.OrderRepository;
import com.challenge.backend.client.ProductFeignClient;
import com.challenge.backend.client.UserFeignClient;
import com.challenge.backend.dto.Items;
import com.challenge.backend.dto.ProductDTO;
import com.challenge.backend.dto.UserDTO;
import com.challenge.backend.entity.OrderEntity;
import com.challenge.backend.request.Id;
import com.challenge.backend.request.RequestOrder;
import com.challenge.backend.response.ResponseCreateOrder;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    UserFeignClient userFeignClient;
    @Autowired
    ProductFeignClient productFeignClient;
    @Autowired
    OrderRepository orderRepository;

    public ResponseCreateOrder createOrder(RequestOrder requestOrder) {
        OrderEntity orderEntity = new OrderEntity();
        Items item = new Items();
        List<Items> itemsList = new ArrayList<>();
        var responseUser = getAll();
        var responseProducts = getProduct();

        var listOfvalidateUser = validadeUser(requestOrder, responseUser);
        List<Integer> productIds = getProductIds(requestOrder);
        List<ProductDTO> matchingProducts = validateProduts(responseProducts, productIds);
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
        for (ProductDTO product : matchingProducts) {
            item.setId(product.getId());
            item.setPrice(product.getPrice());
            item.setAmount(1);  // Definir a quantidade como 1 ou usar um valor fixo

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

    private static List<ProductDTO> validateProduts(List<ProductDTO> responseProducts, List<Integer> productIds) {
        return responseProducts.stream()
                .filter(product -> productIds.contains(product.getId()))
                .collect(Collectors.toList());
    }

    private static List<Integer> getProductIds(RequestOrder requestOrder) {
        return requestOrder.getProducts().stream()
                .map(Id::getId)
                .collect(Collectors.toList());
    }

    private static Optional<UserDTO> validadeUser(RequestOrder requestOrder, List<UserDTO> responseUser) {
        return responseUser.stream().filter(idUser -> requestOrder.getUserID().equals(idUser.getId())).findFirst();
    }

    public List<UserDTO> getAll() {
        var response = userFeignClient.getUsers();
        return response;
    }

    public List<ProductDTO> getProduct() {
        var response = productFeignClient.getProducts();
        return response;
    }
}


