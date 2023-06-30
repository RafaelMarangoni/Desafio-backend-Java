package com.challenge.backend.client;

import com.challenge.backend.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@FeignClient(value = "Products", url = "https://fakestoreapi.com/")
public interface ProductFeignClient {

    @GetMapping("products")
    List<ProductDTO> getProducts();
}
