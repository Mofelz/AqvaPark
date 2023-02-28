package com.example.demo.controllers;

import com.example.demo.models.Post;
import com.example.demo.models.Tarif;
import com.example.demo.repo.PostRepository;
import com.example.demo.repo.TarifRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {

    public final PostRepository postRepository;
    public final TarifRepository tarifRepository;

    public CartController( PostRepository postRepository, TarifRepository tarifRepository) {
        this.postRepository = postRepository;
        this.tarifRepository = tarifRepository;
    }

    @PostMapping("/cart/add")
    public String addToCart(@ModelAttribute("post") Post post, Model model) {
        postRepository.save(post);
        Iterable<Tarif> tarifs = tarifRepository.findAll();
        model.addAttribute("tarifs", tarifs);
        return "blog-add";
    }
}