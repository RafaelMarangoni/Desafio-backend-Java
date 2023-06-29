package com.challenge.backend.client;

import com.challenge.backend.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "Users", url = "https://fakestoreapi.com/")
public interface UserFeignClient {

    @GetMapping("users")
    List<UserDTO> getUsers();
}
