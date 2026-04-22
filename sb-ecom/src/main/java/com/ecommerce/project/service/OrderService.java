package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.*;
import com.ecommerce.project.payload.OrderDTO;
import com.ecommerce.project.repositories.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderService implements iOrderService {

    @Autowired
    private iOrderRepository orderRepository;

    @Autowired
    private iCartRepository cartRepository;

    @Autowired
    private iAddressRepository addressRepository;

    @Autowired
    private iPaymentRepository paymentRepository;

    @Autowired
    private iOrderItemRepository orderItemsRepository;


    @Autowired
    private ModelMapper modelMapper;


    @Override
    @Transactional
    public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod,
                               String pgName, String pgPaymentId, String pgStatus,
                               String pgResponseMessage) {

        //Obtener el carrito del usuario
        Cart cart = cartRepository.findCartByEmail(emailId);
        if(cart == null)
            throw new ResourceNotFoundException("Cart","emailId",emailId);

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Adress","addressId",addressId));


        //Crear una nueva orden con la informacion del pago
        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate((LocalDate.now()));
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted!");
        order.setAddress(address);

        Payment payment = new Payment(paymentMethod, pgPaymentId, pgStatus, pgResponseMessage,pgName);
        payment.setOrder(order);
        payment = paymentRepository.save(payment);
        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);

        //Obtener los items del carrito en los elementos de la orden
        List<CartItem> cartItems = cart.getCartItems();
        if(cartItems.isEmpty())
            throw new APIException("The Cart is empty");

        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setDiscount(item.getDiscount());
            orderItem.setOrderedProductPrice(item.getProductPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }

        orderItems = orderItemsRepository.saveAll(orderItems);

        //--------POST ORDEN---------
        //Actualizar el Stock de Productos
        

        //Limpiar el Carrito

        //Guardar la orden summary

        return null;
    }
}
