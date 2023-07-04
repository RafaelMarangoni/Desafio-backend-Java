package com.challenge.backend.utils;

import com.challenge.backend.client.RequestsClient;
import com.challenge.backend.dto.Items;
import com.challenge.backend.dto.ProductDTO;
import com.challenge.backend.dto.UserDTO;
import com.challenge.backend.request.Id;
import com.challenge.backend.request.RequestOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class Utils {

    @Autowired
    private RequestsClient requestsClient;

    public static List<ProductDTO> validateProduts(List<ProductDTO> responseProducts, List<Integer> productIds) {
        List<ProductDTO> validProducts = new ArrayList<>();

        for (Integer productId : productIds) {
            for (ProductDTO product : responseProducts) {
                if (product.getId()==productId) {
                    validProducts.add(product);
                }
            }
        }

        return validProducts;
    }

    public List<Integer> getProductIds(RequestOrder requestOrder) {
        return requestOrder.getProducts().stream()
                .map(Id::getId)
                .collect(Collectors.toList());
    }
    public List<Integer> getProductUpDateOrder(RequestOrder requestOrder) {
        return requestOrder.getItems().stream()
                .map(Items::getId)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> validadeUser(int userId) {
        var responseUser = requestsClient.getAll();
        return Optional.of(responseUser.stream().filter(idUser -> userId == idUser.getId()).findFirst().orElseThrow());
    }

}
