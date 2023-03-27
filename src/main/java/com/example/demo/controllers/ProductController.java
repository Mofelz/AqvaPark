package com.example.demo.controllers;


import com.example.demo.Service.ProductImageService;
import com.example.demo.models.Product;
import com.example.demo.repo.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class ProductController {
    private final ProductRepository productRepository;

    private final ProductImageService productImageService;

    public ProductController(ProductRepository productRepository, ProductImageService productImageService) {
        this.productRepository = productRepository;
        this.productImageService = productImageService;
    }

    @GetMapping("/products")
    public String productsMain(Model model) {
        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "products/products";
    }

    @GetMapping("/products/add")
    public String productsAdd(@ModelAttribute("products") Product product, Model model) {
        return "products/products-add";
    }

    @PostMapping("/products/add")
    public Object productsAdd(@ModelAttribute("products") @Valid Product product, BindingResult bindingResult,
                              @RequestParam("file") MultipartFile file, Model model) throws IOException {
        if (file.isEmpty()) {
            bindingResult.addError(new ObjectError("image", "Изображение товара не должно быть пустым!"));
            model.addAttribute("errorMessageImage", "Изображение товара не должно быть пустым!");
        }

        if (bindingResult.hasErrors()) return "products/products-add";

        productImageService.saveImageAndProduct(product, file);
        return "redirect:/products";
    }

    @GetMapping("/products/{id}/edit")
    public String tarifEdit(@PathVariable("id") long id, Model model) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный id: " + id));
        model.addAttribute("products", product);
        return "products/products-edit";
    }

    @PostMapping("/products/{id}/edit")
    public String productsUpdate(@PathVariable("id") long id,
                                 @ModelAttribute("products")
                                 @Validated Product product,
                                 @RequestParam("file") MultipartFile file,
                                 BindingResult bindingResult, Model model) throws IOException {
        if (file.isEmpty()) {
            bindingResult.addError(new ObjectError("image", "Изображение товара не должно быть пустым!"));
            model.addAttribute("errorMessageImage", "Изображение товара не должно быть пустым!");
        }
        product.setId(id);
        if (bindingResult.hasErrors()) {
            return "products/products-edit";
        }

        productRepository.save(product);
        productImageService.saveImageAndProduct(product, file);
        return "redirect:/products";
    }

    @PostMapping("/products/{id}/remove")
    public String productsRemove(@PathVariable("id") long id, Model model) {
        Product product = productRepository.findById(id).orElseThrow();
        productRepository.delete(product);
        return "redirect:/products";
    }
}