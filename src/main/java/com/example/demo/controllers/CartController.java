package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.repo.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {

    public final BookingRepository bookingRepository;
    public final ProductRepository productRepository;
    private final StatusRepository statusRepository;

    private final CategoryRepository categoryRepository;

    private final CartRepository cartRepository;

    public CartController(BookingRepository bookingRepository, CategoryRepository categoryRepository, ProductRepository productRepository, StatusRepository statusRepository, CartRepository cartRepository) {
        this.bookingRepository = bookingRepository;
        this.productRepository = productRepository;
        this.statusRepository = statusRepository;
        this.categoryRepository = categoryRepository;

        this.cartRepository = cartRepository;
    }

    @PostMapping("/cart/add")
    public String addToCart(@ModelAttribute("booking") Booking booking, @RequestParam(required = false) Product product, Model model) {

        Iterable<Status> status = statusRepository.findAll();
        model.addAttribute("statuses", status);
        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        Iterable<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        Cart cart;
        if (cartRepository.findCartByBookingAndProduct(booking, product) != null) {
            cart = cartRepository.findCartByBookingAndProduct(booking, product);
            cart.setCount(cart.getCount() + 1);
        } else {
            cart = new Cart(1, booking, product);
        }
        cartRepository.save(cart);
        Iterable<Cart> carts = cartRepository.findAllByBooking(booking);
        int cartCount = 0;
        for(Cart cartt : carts){
            cartCount += cartt.getCount();
        }
        model.addAttribute("cartCount", cartCount);
        return "orders/orders-add";
    }
    @PostMapping("/cart/{id}/remove")
    public String deleteProductCart(@PathVariable("id") long id, Model model) {
        Cart cart = cartRepository.findById(id).orElseThrow();
        cartRepository.delete(cart);
        return "redirect:/orders-edit";
    }
}