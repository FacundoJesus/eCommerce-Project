package com.ecommerce.project.service;

import com.ecommerce.project.payload.AnalyticsResponse;
import com.ecommerce.project.repositories.iOrderRepository;
import com.ecommerce.project.repositories.iProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService implements iAnalyticsService {

    @Autowired
    private iProductRepository productRepository;

    @Autowired
    private iOrderRepository orderRepository;

    @Override
    public AnalyticsResponse getAnalyticsData() {

        AnalyticsResponse analyticsResponse = new AnalyticsResponse();

        Long productCount = productRepository.count();
        Long orderCount = orderRepository.count();
        Double totalRevenue = orderRepository.getTotalRevenue();

        analyticsResponse.setProductCount(String.valueOf(productCount));
        analyticsResponse.setTotalOrders(String.valueOf(orderCount));
        analyticsResponse.setTotalRevenue(String.valueOf(totalRevenue != null ? totalRevenue : 0));

        return analyticsResponse;
    }
}
