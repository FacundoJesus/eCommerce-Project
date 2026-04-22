package com.ecommerce.project.service;

import com.ecommerce.project.payload.OrderDTO;
import jakarta.transaction.Transactional;

public interface iOrderService {

    @Transactional
    OrderDTO placeOrder(String emailId, Long addressId, String pgPaymentId, String pgName, String paymentMethod, String pgStatus, String pgResponseMessage);
}
