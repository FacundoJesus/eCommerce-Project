package com.ecommerce.project.service;

import com.ecommerce.project.payload.OrderDTO;
import com.ecommerce.project.payload.OrderResponse;
import jakarta.transaction.Transactional;

public interface iOrderService {

    @Transactional
    OrderDTO placeOrder(String emailId, Long addressId, String pgPaymentId, String pgName, String paymentMethod, String pgStatus, String pgResponseMessage);

    OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
