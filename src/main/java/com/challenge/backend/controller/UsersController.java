package com.challenge.backend.controller;

import com.challenge.backend.dto.UserDTO;
import com.challenge.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    ResponseEntity<List<UserDTO>> getAllUsers(){
      var responseBody = userService.getAll().getBody();
      return  ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
