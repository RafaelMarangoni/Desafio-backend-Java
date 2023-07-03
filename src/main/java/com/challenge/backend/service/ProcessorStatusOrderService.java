package com.challenge.backend.service;

import com.challenge.backend.Exceptions.BusinessException;
import com.challenge.backend.repository.OrderRepository;
import com.challenge.backend.entity.OrderEntity;
import com.challenge.backend.request.RequestProcessorOrder;
import com.challenge.backend.response.ResponseOrderProcessed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProcessorStatusOrderService {

    @Autowired
    OrderRepository orderRepository;
    @Transactional
    public ResponseOrderProcessed updateOrderStatus(RequestProcessorOrder requestProcessorOrder) {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(requestProcessorOrder.getId());
        if (optionalOrder.isPresent()) {
            OrderEntity order = optionalOrder.get();
            order.setStatus(requestProcessorOrder.getStatus());
            orderRepository.save(order);
            return ResponseOrderProcessed.builder()
                    .message("Order processada com sucesso")
                    .build();
        } else {
           throw new BusinessException("Pedido Não Existe");
        }
    }
}
