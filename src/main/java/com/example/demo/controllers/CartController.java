package com.example.demo.controllers;

import com.example.demo.models.Post;
import com.example.demo.models.Product;
import com.example.demo.repo.PostRepository;
import com.example.demo.repo.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CartController {

    public final PostRepository postRepository;
    public final ProductRepository productRepository;

    public CartController( PostRepository postRepository, ProductRepository productRepository) {
        this.postRepository = postRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/cart/add")
    public String addToCart(@ModelAttribute("post") Post post, Model model) {
        postRepository.save(post);
        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "blog-add";
    }
}