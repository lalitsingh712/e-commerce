package com.lalit.e_commerce.service.impl;

import com.lalit.e_commerce.dto.request.CartRequest;
import com.lalit.e_commerce.entity.Cart;
import com.lalit.e_commerce.entity.CartItem;
import com.lalit.e_commerce.entity.Product;
import com.lalit.e_commerce.entity.User;
import com.lalit.e_commerce.exception.BadRequestException;
import com.lalit.e_commerce.exception.ResourceNotFoundException;
import com.lalit.e_commerce.repository.CartItemRepository;
import com.lalit.e_commerce.repository.CartRepository;
import com.lalit.e_commerce.repository.ProductRepository;
import com.lalit.e_commerce.repository.UserRepository;
import com.lalit.e_commerce.service.CartService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    public CartServiceImpl(CartRepository cartRepo, CartItemRepository cartItemRepo, UserRepository userRepo, ProductRepository productRepo) {
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepo.findByUserId(userId)
                .orElseThrow(()->new ResourceNotFoundException("Cart not found for userId: "+userId));
    }

    @Override
    public Cart createCart(Long userId) {

        if(cartRepo.findByUserId(userId).isPresent()){
            return cartRepo.findByUserId(userId).get();
        }

        User user=userRepo.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User not found with id: "+userId));

        Cart cart=new Cart();
        cart.setUser(user);
        return cartRepo.save(cart);
    }

    private CartRequest convertToDTO(Cart cart){
        CartRequest req=new CartRequest();
        req.setId(cart.getId());
        req.setUserId(cart.getUser().getId());
        req.setItemCount(cart.getCartItems().size());

        return req;
    }

    @Override
    public Cart addProductToCart(Long userId, Long productId, Integer quantity) {

        if(quantity == null || quantity <= 0){
            throw new BadRequestException("Quantity must be greater than 0");
        }

        // Simplified: avoid calling findByUserId twice
        Cart cart = cartRepo.findByUserId(userId)
                .orElseGet(() -> createCart(userId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if(!product.isActive()){
            throw new BadRequestException("Product is not available");
        }

        if(product.getStockQuantity() < quantity){
            throw new BadRequestException("Insufficient product stock");
        }

        CartItem cartItem = cartItemRepo.findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        if(cartItem != null){
            int newQuantity = cartItem.getQuantity() + quantity;
            if(product.getStockQuantity() < newQuantity){
                throw new BadRequestException("Insufficient product stock");
            }
            cartItem.setQuantity(newQuantity);
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cart.getCartItems().add(cartItem);
        }

        return cartRepo.save(cart);
    }

    @Override
    public Cart updateCartItem(Long userId, Long productId, Integer quantity) {

        if(quantity==null || quantity<=0){
            throw new BadRequestException("Quantity must be greater than 0");
        }
        Cart cart=getCartByUserId(userId);

        CartItem cartItem=cartItemRepo.findByCartIdAndProductId(cart.getId(),productId)
                .orElseThrow(()-> new BadRequestException("product not found in cart"));

        Product product=cartItem.getProduct();

        if(product.getStockQuantity()<quantity){
            throw new BadRequestException("Insufficient product stock");
        }

        cartItem.setQuantity(quantity);
        return cartRepo.save(cart);
    }

    @Override
    public void removeProductFromCart(Long userId, Long productId) {

        Cart cart=getCartByUserId(userId);

        CartItem cartItem=cartItemRepo.findByCartIdAndProductId(cart.getId(),productId)
                .orElseThrow(()-> new BadRequestException("Product not found in cart"));

        cart.getCartItems().remove(cartItem);

        cartItemRepo.delete(cartItem);
    }

    @Override
    public void clearCart(Long userId) {

        Cart cart=getCartByUserId(userId);

        cart.getCartItems().clear();

        cartRepo.save(cart);

    }
}
