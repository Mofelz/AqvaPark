package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.repo.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class BlogController {
    private final PostRepository postRepository;

    private final ProductRepository productRepository;

    private final SnackbarRepository snackbarRepository;

    private final UserRepos userRepos;

    public BlogController(PostRepository postRepository, ProductRepository productRepository, SnackbarRepository snackbarRepository, UserRepos userRepos) {
        this.postRepository = postRepository;
        this.productRepository = productRepository;
        this.snackbarRepository = snackbarRepository;
        this.userRepos = userRepos;
    }

    @GetMapping("/orders")
    public String blogMain(@ModelAttribute("post") Post post, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Iterable<Post> posts = postRepository.findAll();
        Iterable<Snackbar> snackbars = snackbarRepository.findAll();
        Iterable<Product> products = productRepository.findAll();
        Iterable<User> users = userRepos.findAll();

        model.addAttribute("products", products);
        model.addAttribute("snackbars", snackbars);
        model.addAttribute("User", users);
        model.addAttribute("posts", posts);

        model.addAttribute("isAdmin", authentication.getAuthorities().toString().contains("ADMIN"));
        model.addAttribute("isSaler", authentication.getAuthorities().toString().contains("SALER"));
        model.addAttribute("isUser", authentication.getAuthorities().toString().contains("USER"));

        return "orders/orders";
    }

    @GetMapping("/orders/add")
    public String blogAdd(@ModelAttribute("post") Post post, Model model) {
        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "orders/orders-add";
    }

    @PostMapping("/orders/add")
    public String blogAdd(@ModelAttribute("post") Post post, BindingResult result, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Post post1 = postRepository.findTopByTableNumberOrderByIdDesc(post.getTableNumber());
        if (post1 != null && post1.getTimeDeparture() == null) {
            result.addError(new ObjectError("tableNumber", "Данный стол уже занят!"));
            model.addAttribute("ErrorMassage", "Данный стол уже занят!");
        }

        if (result.hasErrors()) {

            Iterable<Post> posts = postRepository.findAll();
            Iterable<Snackbar> snackbars = snackbarRepository.findAll();
            Iterable<Product> products = productRepository.findAll();
            Iterable<User> users = userRepos.findAll();

            model.addAttribute("products", products);
            model.addAttribute("snackbars", snackbars);
            model.addAttribute("User", users);
            model.addAttribute("posts", posts);

            model.addAttribute("isAdmin", auth.getAuthorities().toString().contains("ADMIN"));
            model.addAttribute("isSaler", auth.getAuthorities().toString().contains("SALER"));
            model.addAttribute("isUser", auth.getAuthorities().toString().contains("USER"));

            return "orders/orders";
        }
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        model.addAttribute("isAuth", userDetails.getUsername());
        Iterable<Snackbar> snackbars = snackbarRepository.findAll();
        Iterable<Product> products = productRepository.findAll();
        List<User> users = userRepos.findAll();
        model.addAttribute("users", users);
        model.addAttribute("snackbars", snackbars);
        model.addAttribute("products", products);
        post.setTimeArrival(new Date());
        postRepository.save(post);
        return "orders/orders-add";
    }
    @GetMapping("/orders/{id}/edit")
    public String blogEdit(@PathVariable("id") long id, Model model) {
        Iterable<Product> products = productRepository.findAll();
        Post res = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный id: " + id));
        model.addAttribute("post", res);
        model.addAttribute("products", products);

        return "orders/orders-edit";
    }

    @PostMapping("/orders/{id}/edit")
    public String blogPostUpdate(@PathVariable("id") long id,
                                 @ModelAttribute("post")
                                 @Validated Post post, Model model) {

        post.setId(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepos.findByLogin(authentication.getName());
        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        post.setUser(user);
        postRepository.save(post);
        return "redirect:/orders";
    }

    @PostMapping("/orders/{id}/remove")
    public String blogPostRemove(@PathVariable("id") long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);
        return "redirect:/orders";
    }
}