package com.ecommerce.project.service;

import com.ecommerce.project.payload.CartDTO;

public interface iCartService {

    public CartDTO addProductToCart(Long productId, Integer quantity);
}
