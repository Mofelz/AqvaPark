package com.example.demo.controllers;

import com.example.demo.models.Booking;
import com.example.demo.models.Product;
import com.example.demo.repo.BookingRepository;
import com.example.demo.repo.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CartController {

    public final BookingRepository bookingRepository;
    public final ProductRepository productRepository;

    public CartController(BookingRepository bookingRepository, ProductRepository productRepository) {
        this.bookingRepository = bookingRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/cart/add")
    public String addToCart(@ModelAttribute("booking") Booking booking, Model model) {
        bookingRepository.save(booking);
        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "orders/orders-add";
    }
}