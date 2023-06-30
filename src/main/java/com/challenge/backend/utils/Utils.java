package com.challenge.backend.utils;

import com.challenge.backend.dto.ProductDTO;
import com.challenge.backend.dto.UserDTO;
import com.challenge.backend.request.Id;
import com.challenge.backend.request.RequestOrder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class Utils {
    public static List<ProductDTO> validateProduts(List<ProductDTO> responseProducts, List<Integer> productIds) {
        return responseProducts.stream()
                .filter(product -> productIds.contains(product.getId()))
                .collect(Collectors.toList());
    }

    public static List<Integer> getProductIds(RequestOrder requestOrder) {
        return requestOrder.getProducts().stream()
                .map(Id::getId)
                .collect(Collectors.toList());
    }

    public static Optional<UserDTO> validadeUser(RequestOrder requestOrder, List<UserDTO> responseUser) {
        return responseUser.stream().filter(idUser -> requestOrder.getUserID().equals(idUser.getId())).findFirst();
    }
}
