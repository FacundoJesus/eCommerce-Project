package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.repositories.iCartItemRepository;
import com.ecommerce.project.repositories.iCartRepository;
import com.ecommerce.project.repositories.iProductRepository;
import com.ecommerce.project.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartService implements iCartService{

    @Autowired
    iCartRepository cartRepository;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    iProductRepository productRepository;

    @Autowired
    iCartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;



    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {

        Cart cart = createCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId( cart.getCartId(), productId);

        if(cartItem != null)
            throw new APIException("Product " + product.getProductName() + " already exists in the cart.");
        if(product.getQuantity() == 0)
            throw new APIException(product.getProductName() + " is not available.");
        if (product.getQuantity() < quantity)
            throw new APIException("Please, make an order of the " + product.getProductName() +
                    "less than or equal to the quantity " + product.getQuantity() + ".");

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepository.save(newCartItem);
        product.setQuantity(product.getQuantity());

        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));

        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productStream = cartItems.stream().map( item -> {
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });

        cartDTO.setProducts(productStream.toList());

        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {

        List<Cart> carts = cartRepository.findAll();

        if(carts.isEmpty())
            throw new APIException("No cart exists.");

        List<CartDTO> cartDTOs = carts.stream()
                .map(cart -> {CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);

                    List<ProductDTO> products = cart.getCartItems().stream().map(cartItem -> {
                        ProductDTO productDTO = modelMapper.map(cartItem.getProduct(), ProductDTO.class);
                        productDTO.setQuantity(cartItem.getQuantity());
                        return productDTO;
                    }).collect(Collectors.toList());

                cartDTO.setProducts(products);
                return cartDTO;
                }).collect(Collectors.toList());

        return cartDTOs;
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {

        Cart cart = cartRepository.findCartByEmailAndCartId(emailId,cartId);

        if(cart == null)
            throw new ResourceNotFoundException("Cart", "cartId", cartId);

        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);

        cart.getCartItems().forEach(c->c.getProduct().setQuantity(c.getQuantity()));

        List<ProductDTO> productsDTO = cart.getCartItems().stream()
                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
                .collect(Collectors.toList());

        cartDTO.setProducts(productsDTO);

        return cartDTO;
    }


    @Transactional
    @Override
    public CartDTO updateProductQuantityinCart(Long productId, Integer quantity) {

        String emailId = authUtil.loggedInEmail();
        Cart useCart = cartRepository.findCartByEmail(emailId);
        Long cartId = useCart.getCartId();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart","cartId",cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));

        if(product.getQuantity() == 0)
            throw new APIException(product.getProductName() + " is not available.");
        if (product.getQuantity() < quantity)
            throw new APIException("Please, make an order of the " + product.getProductName() +
                    "less than or equal to the quantity " + product.getQuantity() + ".");

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId,productId);

        if(cartItem == null) {
            throw new APIException("Product " + product.getProductName() + " Not available in the Cart.");
        }

        //Calcular nueva cantidad
        int newQuantity = cartItem.getQuantity() + quantity;
        //Validar cantidad negativa
        if (newQuantity < 0)
            throw new APIException("The resulting quantity cannot be negative.");

        if(newQuantity == 0)

            deleteProductFromCart(cartId, productId);
        else {
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setDiscount(product.getDiscount());

            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));

            cartRepository.save(cart);
        }

        CartItem updatedItem = cartItemRepository.save(cartItem);
        if(updatedItem.getQuantity() == 0) {
            cartItemRepository.deleteById(updatedItem.getCartItemId());
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productDTOStream = cartItems.stream().map( item -> {
            ProductDTO productDto = modelMapper.map(item.getProduct(), ProductDTO.class);
            productDto.setQuantity(item.getQuantity());
            return productDto;
        });

        cartDTO.setProducts(productDTOStream.toList());

        return cartDTO;
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart","cartId",cartId));


        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId,productId);
        if(cartItem == null) {
            throw new ResourceNotFoundException("Product","productId",productId);
        }

        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId,productId);

        return "Product " + cartItem.getProduct().getProductName() + " removed from the cart.";

    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {

        //Validaciones
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart","cartId",cartId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId,productId);
        if(cartItem == null) {
            throw new APIException("Product " + product.getProductName() + " not available in the cart.");
        }

        //1000 - 100*2 = 800
        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());

        //200
        cartItem.setProductPrice(product.getSpecialPrice());

        //800 + (200*2) = 1200
        cart.setTotalPrice(cartPrice +
                (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.save(cartItem);
    }


    //Crear carro
    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail((authUtil.loggedInEmail()));
        if(userCart != null)
            return userCart;

        Cart cart = new Cart();
        cart.setTotalPrice(0.0);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepository.save(cart);

        return newCart;
    }
}
