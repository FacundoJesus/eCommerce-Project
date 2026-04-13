package com.ecommerce.project.service;

import com.ecommerce.project.payload.CartDTO;

import java.util.List;

public interface iCartService {

    public CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();
}
