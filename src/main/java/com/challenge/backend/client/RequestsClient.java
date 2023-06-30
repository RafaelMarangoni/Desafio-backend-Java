package com.challenge.backend.client;

import com.challenge.backend.dto.ProductDTO;
import com.challenge.backend.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RequestsClient {


    @Autowired
    UserFeignClient userFeignClient;
    @Autowired
    ProductFeignClient productFeignClient;

    public List<UserDTO> getAll() {
        return userFeignClient.getUsers();
    }

    public List<ProductDTO> getProduct() {
        return productFeignClient.getProducts();
    }
}
