package com.challenge.backend.controller;

import com.challenge.backend.request.RequestOrder;
import com.challenge.backend.request.RequestProcessorOrder;
import com.challenge.backend.response.ResponseCreateOrder;
import com.challenge.backend.response.ResponseOrderProcessed;
import com.challenge.backend.service.OrderService;
import com.challenge.backend.service.ProcessorStatusOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProcessorStatusOrderService processorStatusOrderService;

    @PostMapping("/create-order")
    public ResponseEntity<ResponseCreateOrder> createOrder(@RequestBody RequestOrder requestOrder){
        var response = orderService.createOrder(requestOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/processor-order")
    public ResponseEntity<ResponseOrderProcessed> processedOrder(@RequestBody RequestProcessorOrder requestProcessorOrder){
        var response = processorStatusOrderService.updateOrderStatus(requestProcessorOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
