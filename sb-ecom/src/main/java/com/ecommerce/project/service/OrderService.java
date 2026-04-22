package com.ecommerce.project.service;

import com.ecommerce.project.payload.OrderDTO;
import com.ecommerce.project.repositories.iOrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderService implements iOrderService {

    @Autowired
    private iOrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod,
                               String pgName, String paymentMethod1, String pgStatus,
                               String pgResponseMessage) {
        
        return null;
    }
}
