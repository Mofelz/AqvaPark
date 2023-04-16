package com.example.demo.controllers;


import com.example.demo.models.Category;
import com.example.demo.repo.CategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CategoryController {
    private final CategoryRepository categoryRepository;


    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/category")
    public String categoryMain(Model model) {
        Iterable<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "category/category";
    }

    @GetMapping("/category/add")
    public String categoryAdd(@ModelAttribute("category") Category category, Model model) {
        return "category/category-add";
    }

    @PostMapping("/category/add")
    public Object categoryAdd(@ModelAttribute("category") @Validated Category category, BindingResult bindingResult, Model model) {
        Iterable<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        if (bindingResult.hasErrors()) return "category/category";
        categoryRepository.save(category);
        return "redirect:/category";
    }

    @GetMapping("/category/{id}/edit")
    public String categoryEdit(@ModelAttribute("category") @PathVariable("id") long id, Model model) {
        Category res = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный id: " + id));
        model.addAttribute("category", res);
        return "category/category-edit";
    }

    @PostMapping("/category/{id}/edit")
    public String categoryUpdate(@PathVariable("id") long id, @ModelAttribute("category")
    Category category, BindingResult bindingResult) {
        category.setId(id);
        if (bindingResult.hasErrors()) {
            return "category/category-edit";
        }
        categoryRepository.save(category);
        return "redirect:/category";
    }

    @PostMapping("/category/{id}/remove")
    public String categoryRemove(@PathVariable("id") long id, Model model) {
        Category category = categoryRepository.findById(id).orElseThrow();
        categoryRepository.delete(category);
        return "redirect:/category";
    }
}
