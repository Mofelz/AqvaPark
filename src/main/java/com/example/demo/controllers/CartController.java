package com.example.demo.controllers;

import com.example.demo.models.Booking;
import com.example.demo.models.Category;
import com.example.demo.models.Product;
import com.example.demo.models.Status;
import com.example.demo.repo.BookingRepository;
import com.example.demo.repo.CategoryRepository;
import com.example.demo.repo.ProductRepository;
import com.example.demo.repo.StatusRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CartController {

    public final BookingRepository bookingRepository;
    public final ProductRepository productRepository;
    private final StatusRepository statusRepository;

    private final CategoryRepository categoryRepository;

    public CartController(BookingRepository bookingRepository,CategoryRepository categoryRepository, ProductRepository productRepository,StatusRepository statusRepository) {
        this.bookingRepository = bookingRepository;
        this.productRepository = productRepository;
        this.statusRepository = statusRepository;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/cart/add")
    public String addToCart(@ModelAttribute("booking") Booking booking, Model model) {

        Iterable<Status> status = statusRepository.findAll();
        model.addAttribute("statuses", status);
        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        Iterable<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        bookingRepository.save(booking);
        return "orders/orders-add";
    }
}