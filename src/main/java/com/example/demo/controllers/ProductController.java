package com.example.demo.controllers;


import com.example.demo.Service.ProductImageService;
import com.example.demo.models.Category;
import com.example.demo.models.Product;
import com.example.demo.models.ProductSort;
import com.example.demo.repo.CategoryRepository;
import com.example.demo.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
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
@PreAuthorize("hasAnyAuthority('SALER')")
public class ProductController {

    private final ProductRepository productRepository;

    private final ProductImageService productImageService;

    private final CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository, ProductImageService productImageService, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productImageService = productImageService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/products")
    public String productsMain(Model model) {
        Iterable<Product> products = productRepository.findAll(PageRequest.of(0, 5));
        int size = 0;
        for (Product product : productRepository.findAll()){
            size+=1;
        }
        size = size/5;
        model.addAttribute("size",size);
        model.addAttribute("products", products);
        model.addAttribute("offset",0);
        return "products/products";
    }
    @PostMapping("/hiddenDish")
    public String hiddenDish(@RequestParam Product product) {
        product.setHiddenDish(!product.isHiddenDish());
        productRepository.save(product);
        return "redirect:/products";
    }
    @GetMapping("/products/add")
    public String productsAdd(@ModelAttribute("products") Product product, Model model) {
        Iterable<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "products/products-add";
    }

    @GetMapping("productsSort")
    public String getAllAndEnumSort(
            @RequestParam("offset") Integer offset, Model model){
        offset-=1;
        Iterable<Product> products;
        if(offset < 0){
            offset = 0;
            products = productRepository.findAll(PageRequest.of(offset, 5));
        }
        else {
            products = productRepository.findAll(PageRequest.of(offset, 5));
        }
        int size = 0;
        for (Product product : productRepository.findAll()){
            size+=1;
        }
        size = (int) Math.ceil(size/5);
        if(size < offset){
            offset = size;
            products = productRepository.findAll(PageRequest.of(offset, 5));
        }
        model.addAttribute("size",size);
        model.addAttribute("offset",offset);
        model.addAttribute("products",products);
        return "products/products";
    }

    @PostMapping("/products/add")
    public Object productsAdd(@ModelAttribute("products") @Valid Product product, BindingResult bindingResult,
                              @RequestParam("file") MultipartFile file, Model model) throws IOException {
        if (file.isEmpty()) {
            bindingResult.addError(new ObjectError("image", "Изображение товара не должно быть пустым!"));
            model.addAttribute("errorMessageImage", "Изображение товара не должно быть пустым!");
        }
        if (bindingResult.hasErrors()) return "products/products-add";
        Iterable<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        productImageService.saveImageAndProduct(product, file);
        return "redirect:/products";
    }

    @GetMapping("/products/{id}/edit")
    public String productsEdit(@PathVariable("id") long id, Model model) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный id: " + id));
        model.addAttribute("products", product);
        Iterable<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);

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
        Iterable<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
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
