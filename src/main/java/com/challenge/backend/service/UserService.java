package com.challenge.backend.service;

import com.challenge.backend.client.UserFeignClient;
import com.challenge.backend.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserFeignClient userFeignClient;

    public ResponseEntity<List<UserDTO>> getAll(){
        userFeignClient.getUsers();
        return null;
    }
}
