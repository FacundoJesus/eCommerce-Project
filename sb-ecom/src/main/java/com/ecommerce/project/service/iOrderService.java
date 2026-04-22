package com.ecommerce.project.service;

import com.ecommerce.project.payload.OrderDTO;

public interface iOrderService {
    OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String paymentMethod1, String pgStatus, String pgResponseMessage);
}
